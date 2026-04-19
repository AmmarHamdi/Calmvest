package com.calmvest.core.domain.model

enum class OrderStatus {
    PENDING, EXECUTED, CANCELLED, FAILED
}

data class InvestmentOrder(
    val id: String,
    val userId: String,
    val goalId: String?,
    val amountMinorUnits: Long,
    val mode: InvestmentMode,
    val status: OrderStatus,
    val executedAt: String?,
    val createdAt: String
) {
    val amount: Money get() = Money.ofMinorUnits(amountMinorUnits)
}
