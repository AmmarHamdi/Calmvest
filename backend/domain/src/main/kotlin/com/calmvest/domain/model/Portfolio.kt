package com.calmvest.domain.model

import java.time.Instant

data class Portfolio(
    val id: PortfolioId,
    val userId: UserId,
    val mode: InvestmentMode,
    val totalInvested: Money,
    val currentValue: Money,
    val createdAt: Instant,
    val updatedAt: Instant
)
