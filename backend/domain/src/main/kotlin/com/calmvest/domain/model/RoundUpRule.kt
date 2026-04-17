package com.calmvest.domain.model

import java.time.Instant

data class RoundUpRule(
    val id: RoundUpRuleId,
    val userId: UserId,
    val isEnabled: Boolean,
    val monthlyCapAmount: Money,
    val currentMonthAccumulated: Money,
    val pausedUntil: Instant?,
    val investmentThreshold: Money,
    val createdAt: Instant,
    val updatedAt: Instant
)
