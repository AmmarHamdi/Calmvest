package com.calmvest.core.data.remote.dto

import com.calmvest.core.domain.model.*

fun UserDto.toDomain() = User(
    id = id,
    email = email,
    firstName = firstName,
    lastName = lastName,
    createdAt = createdAt
)

fun BankAccountDto.toDomain() = BankAccount(
    id = id,
    userId = userId,
    bankName = bankName,
    accountNumberMasked = accountNumberMasked,
    iban = iban,
    isActive = isActive,
    linkedAt = linkedAt
)

fun TransactionDto.toDomain() = Transaction(
    id = id,
    userId = userId,
    bankAccountId = bankAccountId,
    merchantName = merchantName,
    amount = Money.ofMinorUnits(amountMinorUnits),
    roundUpAmount = Money.ofMinorUnits(roundUpAmountMinorUnits),
    transactionType = when (transactionType.uppercase()) {
        "CREDIT" -> TransactionType.CREDIT
        else -> TransactionType.DEBIT
    },
    date = date,
    isRoundedUp = isRoundedUp
)

fun GoalDto.toDomain() = Goal(
    id = id,
    userId = userId,
    name = name,
    goalType = when (goalType.uppercase()) {
        "TRAVEL" -> GoalType.TRAVEL
        "EMERGENCY_FUND" -> GoalType.EMERGENCY_FUND
        "CAR" -> GoalType.CAR
        "HOUSE" -> GoalType.HOUSE
        "EDUCATION" -> GoalType.EDUCATION
        else -> GoalType.OTHER
    },
    targetAmount = Money.ofMinorUnits(targetAmountMinorUnits),
    currentAmount = Money.ofMinorUnits(currentAmountMinorUnits),
    targetDate = targetDate,
    isActive = isActive,
    createdAt = createdAt
)

fun RoundUpRuleDto.toDomain() = RoundUpRule(
    id = id,
    userId = userId,
    isEnabled = isEnabled,
    monthlyCapMinorUnits = monthlyCapMinorUnits,
    thresholdMinorUnits = thresholdMinorUnits,
    pauseUntil = pauseUntil
)

fun InvestmentOrderDto.toDomain() = InvestmentOrder(
    id = id,
    userId = userId,
    goalId = goalId,
    amountMinorUnits = amountMinorUnits,
    mode = when (mode.uppercase()) {
        "BITCOIN" -> InvestmentMode.BITCOIN
        "DIVERSIFIED" -> InvestmentMode.DIVERSIFIED
        else -> InvestmentMode.SAFE
    },
    status = when (status.uppercase()) {
        "EXECUTED" -> OrderStatus.EXECUTED
        "CANCELLED" -> OrderStatus.CANCELLED
        "FAILED" -> OrderStatus.FAILED
        else -> OrderStatus.PENDING
    },
    executedAt = executedAt,
    createdAt = createdAt
)

fun PortfolioDto.toDomain() = Portfolio(
    id = id,
    userId = userId,
    mode = when (mode.uppercase()) {
        "BITCOIN" -> InvestmentMode.BITCOIN
        "DIVERSIFIED" -> InvestmentMode.DIVERSIFIED
        else -> InvestmentMode.SAFE
    },
    totalInvestedMinorUnits = totalInvestedMinorUnits,
    currentValueMinorUnits = currentValueMinorUnits
)

fun ReserveSummaryDto.toDomain() = ReserveSummary(
    userId = userId,
    totalReserveMinorUnits = totalReserveMinorUnits,
    pendingRoundUpsMinorUnits = pendingRoundUpsMinorUnits,
    thisMonthRoundUpsMinorUnits = thisMonthRoundUpsMinorUnits
)
