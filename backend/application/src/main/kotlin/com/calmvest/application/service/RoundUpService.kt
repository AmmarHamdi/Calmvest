package com.calmvest.application.service

import com.calmvest.application.dto.ImportTransactionRequest
import com.calmvest.application.dto.TransactionDto
import com.calmvest.domain.model.BankAccountId
import com.calmvest.domain.model.Money
import com.calmvest.domain.model.ReserveEntry
import com.calmvest.domain.model.ReserveEntryId
import com.calmvest.domain.model.Transaction
import com.calmvest.domain.model.TransactionId
import com.calmvest.domain.model.UserId
import com.calmvest.domain.repository.BankAccountRepository
import com.calmvest.domain.repository.ReserveEntryRepository
import com.calmvest.domain.repository.RoundUpRuleRepository
import com.calmvest.domain.repository.TransactionRepository
import com.calmvest.domain.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.util.UUID

@Service
class RoundUpService(
    private val transactionRepository: TransactionRepository,
    private val reserveEntryRepository: ReserveEntryRepository,
    private val roundUpRuleRepository: RoundUpRuleRepository,
    private val bankAccountRepository: BankAccountRepository,
    private val userRepository: UserRepository,
    private val investmentOrderService: InvestmentOrderService
) {

    @Transactional
    fun importTransaction(userId: UUID, request: ImportTransactionRequest): TransactionDto {
        require(request.idempotencyKey.isNotBlank()) { "Idempotency key must not be blank" }
        require(request.amountEuros > java.math.BigDecimal.ZERO) { "Amount must be positive" }

        // Idempotency check: return existing result if already processed
        val existing = transactionRepository.findByIdempotencyKey(request.idempotencyKey)
        if (existing.isPresent) return existing.get().toDto()

        val uid = UserId(userId)
        userRepository.findById(uid)
            .orElseThrow { NoSuchElementException("User not found: $userId") }

        val bankAccountId = BankAccountId(request.bankAccountId)
        bankAccountRepository.findById(bankAccountId)
            .orElseThrow { NoSuchElementException("Bank account not found: ${request.bankAccountId}") }

        val amount = Money.ofEuros(request.amountEuros)
        val roundUpAmount = amount.roundUpToNextEuro()

        val transaction = Transaction(
            id = TransactionId.generate(),
            bankAccountId = bankAccountId,
            userId = uid,
            amount = amount,
            roundUpAmount = roundUpAmount,
            description = request.description,
            merchantName = request.merchantName,
            transactedAt = request.transactedAt,
            importedAt = Instant.now(),
            idempotencyKey = request.idempotencyKey
        )
        val saved = transactionRepository.save(transaction)

        processRoundUp(uid, saved)

        return saved.toDto()
    }

    private fun processRoundUp(userId: UserId, transaction: Transaction) {
        if (transaction.roundUpAmount.isZero()) return

        val rule = roundUpRuleRepository.findByUserId(userId).orElse(null) ?: return
        if (!rule.isEnabled) return

        val now = Instant.now()
        if (rule.pausedUntil != null && rule.pausedUntil!!.isAfter(now)) return

        val effectiveRoundUp = if (
            rule.currentMonthAccumulated + transaction.roundUpAmount > rule.monthlyCapAmount
        ) {
            rule.monthlyCapAmount - rule.currentMonthAccumulated
        } else {
            transaction.roundUpAmount
        }

        if (!effectiveRoundUp.isPositive()) return

        val reserveIdempotencyKey = "reserve:${transaction.idempotencyKey}"
        if (reserveEntryRepository.existsByIdempotencyKey(reserveIdempotencyKey)) return

        val entry = ReserveEntry(
            id = ReserveEntryId.generate(),
            userId = userId,
            transactionId = transaction.id,
            amount = effectiveRoundUp,
            createdAt = now,
            idempotencyKey = reserveIdempotencyKey
        )
        reserveEntryRepository.save(entry)

        val updatedRule = rule.copy(
            currentMonthAccumulated = rule.currentMonthAccumulated + effectiveRoundUp,
            updatedAt = now
        )
        roundUpRuleRepository.save(updatedRule)

        val totalReserve = reserveEntryRepository.sumAmountByUserId(userId)
        if (totalReserve >= rule.investmentThreshold) {
            investmentOrderService.triggerAutomaticInvestment(userId, totalReserve, rule)
        }
    }

    @Transactional(readOnly = true)
    fun getTransactionsForUser(userId: UUID): List<TransactionDto> {
        val uid = UserId(userId)
        userRepository.findById(uid)
            .orElseThrow { NoSuchElementException("User not found: $userId") }
        return transactionRepository.findByUserId(uid).map { it.toDto() }
    }

    private fun Transaction.toDto() = TransactionDto(
        id = id.value,
        bankAccountId = bankAccountId.value,
        userId = userId.value,
        amountEuros = amount.toEuros(),
        roundUpAmountEuros = roundUpAmount.toEuros(),
        description = description,
        merchantName = merchantName,
        transactedAt = transactedAt,
        importedAt = importedAt,
        idempotencyKey = idempotencyKey
    )
}
