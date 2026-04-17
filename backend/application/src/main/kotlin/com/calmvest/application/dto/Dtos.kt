package com.calmvest.application.dto

import com.calmvest.domain.model.KycStatus
import java.math.BigDecimal
import java.time.Instant
import java.util.UUID

data class UserDto(
    val id: UUID,
    val email: String,
    val name: String,
    val kycStatus: KycStatus,
    val createdAt: Instant,
    val updatedAt: Instant
)

data class CreateUserRequest(
    val email: String,
    val name: String
)

data class BankAccountDto(
    val id: UUID,
    val userId: UUID,
    val iban: String,
    val provider: String,
    val consentId: String,
    val isActive: Boolean,
    val createdAt: Instant
)

data class CreateBankAccountRequest(
    val iban: String,
    val provider: String,
    val consentId: String
)

data class TransactionDto(
    val id: UUID,
    val bankAccountId: UUID,
    val userId: UUID,
    val amountEuros: BigDecimal,
    val roundUpAmountEuros: BigDecimal,
    val description: String,
    val merchantName: String?,
    val transactedAt: Instant,
    val importedAt: Instant,
    val idempotencyKey: String
)

data class ImportTransactionRequest(
    val bankAccountId: UUID,
    val amountEuros: BigDecimal,
    val description: String,
    val merchantName: String?,
    val transactedAt: Instant,
    val idempotencyKey: String
)

data class RoundUpRuleDto(
    val id: UUID,
    val userId: UUID,
    val isEnabled: Boolean,
    val monthlyCapEuros: BigDecimal,
    val currentMonthAccumulatedEuros: BigDecimal,
    val pausedUntil: Instant?,
    val investmentThresholdEuros: BigDecimal,
    val createdAt: Instant,
    val updatedAt: Instant
)

data class UpdateRoundUpRuleRequest(
    val isEnabled: Boolean,
    val monthlyCapEuros: BigDecimal,
    val pausedUntil: Instant?,
    val investmentThresholdEuros: BigDecimal
)

data class ReserveDto(
    val userId: UUID,
    val totalAmountEuros: BigDecimal,
    val entries: List<ReserveEntryDto>
)

data class ReserveEntryDto(
    val id: UUID,
    val transactionId: UUID,
    val amountEuros: BigDecimal,
    val createdAt: Instant,
    val idempotencyKey: String
)

data class GoalDto(
    val id: UUID,
    val userId: UUID,
    val name: String,
    val description: String?,
    val targetAmountEuros: BigDecimal,
    val currentAmountEuros: BigDecimal,
    val targetDate: String?,
    val status: String,
    val createdAt: Instant,
    val updatedAt: Instant
)

data class CreateGoalRequest(
    val name: String,
    val description: String?,
    val targetAmountEuros: BigDecimal,
    val targetDate: String?
)

data class UpdateGoalRequest(
    val name: String?,
    val description: String?,
    val targetAmountEuros: BigDecimal?,
    val targetDate: String?,
    val status: String?
)

data class InvestmentOrderDto(
    val id: UUID,
    val userId: UUID,
    val goalId: UUID,
    val amountEuros: BigDecimal,
    val mode: String,
    val status: String,
    val idempotencyKey: String,
    val providerOrderId: String?,
    val createdAt: Instant,
    val executedAt: Instant?,
    val updatedAt: Instant
)

data class CreateInvestmentOrderRequest(
    val goalId: UUID,
    val amountEuros: BigDecimal,
    val mode: String,
    val idempotencyKey: String
)

data class PortfolioDto(
    val id: UUID,
    val userId: UUID,
    val mode: String,
    val totalInvestedEuros: BigDecimal,
    val currentValueEuros: BigDecimal,
    val createdAt: Instant,
    val updatedAt: Instant
)
