package com.calmvest.domain.model

import java.time.Instant

data class InvestmentOrder(
    val id: InvestmentOrderId,
    val userId: UserId,
    val goalId: GoalId,
    val amount: Money,
    val mode: InvestmentMode,
    val status: OrderStatus,
    val idempotencyKey: String,
    val providerOrderId: String?,
    val createdAt: Instant,
    val executedAt: Instant?,
    val updatedAt: Instant
)

enum class InvestmentMode { SAFE, BITCOIN, DIVERSIFIED }

enum class OrderStatus { PENDING, SUBMITTED, EXECUTED, FAILED, CANCELLED }
