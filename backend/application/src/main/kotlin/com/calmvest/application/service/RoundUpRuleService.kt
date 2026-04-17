package com.calmvest.application.service

import com.calmvest.application.dto.ReserveDto
import com.calmvest.application.dto.ReserveEntryDto
import com.calmvest.application.dto.RoundUpRuleDto
import com.calmvest.application.dto.UpdateRoundUpRuleRequest
import com.calmvest.domain.model.Money
import com.calmvest.domain.model.RoundUpRule
import com.calmvest.domain.model.RoundUpRuleId
import com.calmvest.domain.model.UserId
import com.calmvest.domain.repository.ReserveEntryRepository
import com.calmvest.domain.repository.RoundUpRuleRepository
import com.calmvest.domain.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.util.UUID

@Service
class RoundUpRuleService(
    private val roundUpRuleRepository: RoundUpRuleRepository,
    private val reserveEntryRepository: ReserveEntryRepository,
    private val userRepository: UserRepository
) {

    @Transactional(readOnly = true)
    fun getRoundUpRule(userId: UUID): RoundUpRuleDto {
        val uid = UserId(userId)
        userRepository.findById(uid)
            .orElseThrow { NoSuchElementException("User not found: $userId") }
        return roundUpRuleRepository.findByUserId(uid)
            .map { it.toDto() }
            .orElseGet { createDefaultRule(uid).toDto() }
    }

    @Transactional
    fun upsertRoundUpRule(userId: UUID, request: UpdateRoundUpRuleRequest): RoundUpRuleDto {
        require(request.monthlyCapEuros > java.math.BigDecimal.ZERO) {
            "Monthly cap must be positive"
        }
        require(request.investmentThresholdEuros > java.math.BigDecimal.ZERO) {
            "Investment threshold must be positive"
        }

        val uid = UserId(userId)
        userRepository.findById(uid)
            .orElseThrow { NoSuchElementException("User not found: $userId") }

        val now = Instant.now()
        val existing = roundUpRuleRepository.findByUserId(uid)
        val rule = if (existing.isPresent) {
            existing.get().copy(
                isEnabled = request.isEnabled,
                monthlyCapAmount = Money.ofEuros(request.monthlyCapEuros),
                pausedUntil = request.pausedUntil,
                investmentThreshold = Money.ofEuros(request.investmentThresholdEuros),
                updatedAt = now
            )
        } else {
            createDefaultRule(uid).copy(
                isEnabled = request.isEnabled,
                monthlyCapAmount = Money.ofEuros(request.monthlyCapEuros),
                pausedUntil = request.pausedUntil,
                investmentThreshold = Money.ofEuros(request.investmentThresholdEuros),
                updatedAt = now
            )
        }
        return roundUpRuleRepository.save(rule).toDto()
    }

    @Transactional(readOnly = true)
    fun getReserve(userId: UUID): ReserveDto {
        val uid = UserId(userId)
        userRepository.findById(uid)
            .orElseThrow { NoSuchElementException("User not found: $userId") }

        val entries = reserveEntryRepository.findByUserId(uid)
        val total = reserveEntryRepository.sumAmountByUserId(uid)

        return ReserveDto(
            userId = userId,
            totalAmountEuros = total.toEuros(),
            entries = entries.map { e ->
                ReserveEntryDto(
                    id = e.id.value,
                    transactionId = e.transactionId.value,
                    amountEuros = e.amount.toEuros(),
                    createdAt = e.createdAt,
                    idempotencyKey = e.idempotencyKey
                )
            }
        )
    }

    private fun createDefaultRule(userId: UserId): RoundUpRule {
        val now = Instant.now()
        return RoundUpRule(
            id = RoundUpRuleId.generate(),
            userId = userId,
            isEnabled = false,
            monthlyCapAmount = Money.ofEuros(java.math.BigDecimal("50.00")),
            currentMonthAccumulated = Money.zero(),
            pausedUntil = null,
            investmentThreshold = Money.ofEuros(java.math.BigDecimal("10.00")),
            createdAt = now,
            updatedAt = now
        )
    }

    private fun RoundUpRule.toDto() = RoundUpRuleDto(
        id = id.value,
        userId = userId.value,
        isEnabled = isEnabled,
        monthlyCapEuros = monthlyCapAmount.toEuros(),
        currentMonthAccumulatedEuros = currentMonthAccumulated.toEuros(),
        pausedUntil = pausedUntil,
        investmentThresholdEuros = investmentThreshold.toEuros(),
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}
