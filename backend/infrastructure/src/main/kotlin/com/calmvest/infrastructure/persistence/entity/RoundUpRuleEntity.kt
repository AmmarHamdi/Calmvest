package com.calmvest.infrastructure.persistence.entity

import jakarta.persistence.*
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "round_up_rules")
class RoundUpRuleEntity(
    @Id
    @Column(name = "id", updatable = false, nullable = false)
    val id: UUID,

    @Column(name = "user_id", nullable = false, unique = true)
    val userId: UUID,

    @Column(name = "is_enabled", nullable = false)
    var isEnabled: Boolean,

    @Column(name = "monthly_cap_minor_units", nullable = false)
    var monthlyCapMinorUnits: Long,

    @Column(name = "current_month_accumulated_minor_units", nullable = false)
    var currentMonthAccumulatedMinorUnits: Long,

    @Column(name = "paused_until")
    var pausedUntil: Instant?,

    @Column(name = "investment_threshold_minor_units", nullable = false)
    var investmentThresholdMinorUnits: Long,

    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: Instant,

    @Column(name = "updated_at", nullable = false)
    var updatedAt: Instant
)
