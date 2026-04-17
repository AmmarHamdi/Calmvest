package com.calmvest.infrastructure.persistence.entity

import jakarta.persistence.*
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "portfolios")
class PortfolioEntity(
    @Id
    @Column(name = "id", updatable = false, nullable = false)
    val id: UUID,

    @Column(name = "user_id", nullable = false)
    val userId: UUID,

    @Column(name = "mode", nullable = false)
    @Enumerated(EnumType.STRING)
    val mode: InvestmentModeEntity,

    @Column(name = "total_invested_minor_units", nullable = false)
    var totalInvestedMinorUnits: Long,

    @Column(name = "current_value_minor_units", nullable = false)
    var currentValueMinorUnits: Long,

    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: Instant,

    @Column(name = "updated_at", nullable = false)
    var updatedAt: Instant
)
