package com.calmvest.application.dto

import com.calmvest.domain.model.KycStatus
import jakarta.validation.constraints.DecimalMin
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
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
    @field:NotBlank(message = "Email must not be blank")
    @field:Email(message = "Email must be a valid email address")
    @field:Size(max = 255, message = "Email must not exceed 255 characters")
    val email: String,

    @field:NotBlank(message = "Name must not be blank")
    @field:Size(min = 1, max = 255, message = "Name must be between 1 and 255 characters")
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
    @field:NotBlank(message = "IBAN must not be blank")
    @field:Size(max = 34, message = "IBAN must not exceed 34 characters")
    val iban: String,

    @field:NotBlank(message = "Provider must not be blank")
    @field:Size(max = 100, message = "Provider must not exceed 100 characters")
    val provider: String,

    @field:NotBlank(message = "Consent ID must not be blank")
    @field:Size(max = 255, message = "Consent ID must not exceed 255 characters")
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
    @field:NotNull(message = "Bank account ID must not be null")
    val bankAccountId: UUID,

    @field:NotNull(message = "Amount must not be null")
    @field:DecimalMin(value = "0.01", message = "Amount must be at least 0.01")
    val amountEuros: BigDecimal,

    @field:NotBlank(message = "Description must not be blank")
    val description: String,

    val merchantName: String?,

    @field:NotNull(message = "Transaction date must not be null")
    val transactedAt: Instant,

    @field:NotBlank(message = "Idempotency key must not be blank")
    @field:Size(max = 255, message = "Idempotency key must not exceed 255 characters")
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
    @field:NotNull(message = "isEnabled must not be null")
    val isEnabled: Boolean,

    @field:NotNull(message = "Monthly cap must not be null")
    @field:DecimalMin(value = "0.01", message = "Monthly cap must be at least 0.01")
    val monthlyCapEuros: BigDecimal,

    val pausedUntil: Instant?,

    @field:NotNull(message = "Investment threshold must not be null")
    @field:DecimalMin(value = "0.01", message = "Investment threshold must be at least 0.01")
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
    @field:NotBlank(message = "Goal name must not be blank")
    @field:Size(max = 255, message = "Goal name must not exceed 255 characters")
    val name: String,

    @field:Size(max = 1000, message = "Description must not exceed 1000 characters")
    val description: String?,

    @field:NotNull(message = "Target amount must not be null")
    @field:DecimalMin(value = "0.01", message = "Target amount must be at least 0.01")
    val targetAmountEuros: BigDecimal,

    val targetDate: String?
)

data class UpdateGoalRequest(
    @field:Size(max = 255, message = "Goal name must not exceed 255 characters")
    val name: String?,

    @field:Size(max = 1000, message = "Description must not exceed 1000 characters")
    val description: String?,

    @field:DecimalMin(value = "0.01", message = "Target amount must be at least 0.01")
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
    @field:NotNull(message = "Goal ID must not be null")
    val goalId: UUID,

    @field:NotNull(message = "Amount must not be null")
    @field:DecimalMin(value = "0.01", message = "Amount must be at least 0.01")
    val amountEuros: BigDecimal,

    @field:NotBlank(message = "Mode must not be blank")
    val mode: String,

    @field:NotBlank(message = "Idempotency key must not be blank")
    @field:Size(max = 255, message = "Idempotency key must not exceed 255 characters")
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
