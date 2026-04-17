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
import org.slf4j.LoggerFactory
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

    private val log = LoggerFactory.getLogger(RoundUpRuleService::class.java)

    @Transactional(readOnly = true)
    fun getRoundUpRule(userId: UUID): RoundUpRuleDto {
        val uid = UserId(userId)
        userRepository.findById(uid)
            .orElseThrow { NoSuchElementException("User not found: $userId") }
        return roundUpRuleRepository.findByUserId(uid)
            .map { it.toDto() }
            .orElseGet { defaultRuleDto(uid) }
    }

    @Transactional
    fun upsertRoundUpRule(userId: UUID, request: UpdateRoundUpRuleRequest): RoundUpRuleDto {
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
            RoundUpRule(
                id = RoundUpRuleId.generate(),
                userId = uid,
                isEnabled = request.isEnabled,
                monthlyCapAmount = Money.ofEuros(request.monthlyCapEuros),
                currentMonthAccumulated = Money.zero(),
                pausedUntil = request.pausedUntil,
                investmentThreshold = Money.ofEuros(request.investmentThresholdEuros),
                createdAt = now,
                updatedAt = now
            )
        }
        log.info("Upserting round-up rule for user {}: enabled={}", userId, rule.isEnabled)
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

    private fun defaultRuleDto(userId: UserId): RoundUpRuleDto {
        val now = Instant.now()
        return RoundUpRuleDto(
            id = UUID.randomUUID(),
            userId = userId.value,
            isEnabled = false,
            monthlyCapEuros = java.math.BigDecimal("50.00"),
            currentMonthAccumulatedEuros = java.math.BigDecimal.ZERO,
            pausedUntil = null,
            investmentThresholdEuros = java.math.BigDecimal("10.00"),
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
