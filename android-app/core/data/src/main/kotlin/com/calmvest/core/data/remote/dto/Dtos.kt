package com.calmvest.core.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserDto(
    @Json(name = "id") val id: String,
    @Json(name = "email") val email: String,
    @Json(name = "first_name") val firstName: String,
    @Json(name = "last_name") val lastName: String,
    @Json(name = "created_at") val createdAt: String
)

@JsonClass(generateAdapter = true)
data class BankAccountDto(
    @Json(name = "id") val id: String,
    @Json(name = "user_id") val userId: String,
    @Json(name = "bank_name") val bankName: String,
    @Json(name = "account_number_masked") val accountNumberMasked: String,
    @Json(name = "iban") val iban: String,
    @Json(name = "is_active") val isActive: Boolean,
    @Json(name = "linked_at") val linkedAt: String
)

@JsonClass(generateAdapter = true)
data class TransactionDto(
    @Json(name = "id") val id: String,
    @Json(name = "user_id") val userId: String,
    @Json(name = "bank_account_id") val bankAccountId: String,
    @Json(name = "merchant_name") val merchantName: String,
    @Json(name = "amount_minor_units") val amountMinorUnits: Long,
    @Json(name = "round_up_amount_minor_units") val roundUpAmountMinorUnits: Long,
    @Json(name = "transaction_type") val transactionType: String,
    @Json(name = "date") val date: String,
    @Json(name = "is_rounded_up") val isRoundedUp: Boolean
)

@JsonClass(generateAdapter = true)
data class GoalDto(
    @Json(name = "id") val id: String,
    @Json(name = "user_id") val userId: String,
    @Json(name = "name") val name: String,
    @Json(name = "goal_type") val goalType: String,
    @Json(name = "target_amount_minor_units") val targetAmountMinorUnits: Long,
    @Json(name = "current_amount_minor_units") val currentAmountMinorUnits: Long,
    @Json(name = "target_date") val targetDate: String?,
    @Json(name = "is_active") val isActive: Boolean,
    @Json(name = "created_at") val createdAt: String
)

@JsonClass(generateAdapter = true)
data class RoundUpRuleDto(
    @Json(name = "id") val id: String,
    @Json(name = "user_id") val userId: String,
    @Json(name = "is_enabled") val isEnabled: Boolean,
    @Json(name = "monthly_cap_minor_units") val monthlyCapMinorUnits: Long,
    @Json(name = "threshold_minor_units") val thresholdMinorUnits: Long,
    @Json(name = "pause_until") val pauseUntil: String?
)

@JsonClass(generateAdapter = true)
data class InvestmentOrderDto(
    @Json(name = "id") val id: String,
    @Json(name = "user_id") val userId: String,
    @Json(name = "goal_id") val goalId: String?,
    @Json(name = "amount_minor_units") val amountMinorUnits: Long,
    @Json(name = "mode") val mode: String,
    @Json(name = "status") val status: String,
    @Json(name = "executed_at") val executedAt: String?,
    @Json(name = "created_at") val createdAt: String
)

@JsonClass(generateAdapter = true)
data class PortfolioDto(
    @Json(name = "id") val id: String,
    @Json(name = "user_id") val userId: String,
    @Json(name = "mode") val mode: String,
    @Json(name = "total_invested_minor_units") val totalInvestedMinorUnits: Long,
    @Json(name = "current_value_minor_units") val currentValueMinorUnits: Long
)

@JsonClass(generateAdapter = true)
data class ReserveSummaryDto(
    @Json(name = "user_id") val userId: String,
    @Json(name = "total_reserve_minor_units") val totalReserveMinorUnits: Long,
    @Json(name = "pending_round_ups_minor_units") val pendingRoundUpsMinorUnits: Long,
    @Json(name = "this_month_round_ups_minor_units") val thisMonthRoundUpsMinorUnits: Long
)

@JsonClass(generateAdapter = true)
data class CreateGoalRequest(
    @Json(name = "name") val name: String,
    @Json(name = "goal_type") val goalType: String,
    @Json(name = "target_amount_minor_units") val targetAmountMinorUnits: Long,
    @Json(name = "target_date") val targetDate: String?
)

@JsonClass(generateAdapter = true)
data class UpdateRoundUpRuleRequest(
    @Json(name = "is_enabled") val isEnabled: Boolean,
    @Json(name = "monthly_cap_minor_units") val monthlyCapMinorUnits: Long,
    @Json(name = "threshold_minor_units") val thresholdMinorUnits: Long,
    @Json(name = "pause_until") val pauseUntil: String?
)

@JsonClass(generateAdapter = true)
data class UpdateInvestmentModeRequest(
    @Json(name = "mode") val mode: String
)
