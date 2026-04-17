package com.calmvest.infrastructure.persistence.entity

import jakarta.persistence.*
import java.time.Instant
import java.time.LocalDate
import java.util.UUID

@Entity
@Table(name = "goals")
class GoalEntity(
    @Id
    @Column(name = "id", updatable = false, nullable = false)
    val id: UUID,

    @Column(name = "user_id", nullable = false)
    val userId: UUID,

    @Column(name = "name", nullable = false)
    var name: String,

    @Column(name = "description")
    var description: String?,

    @Column(name = "target_amount_minor_units", nullable = false)
    var targetAmountMinorUnits: Long,

    @Column(name = "current_amount_minor_units", nullable = false)
    var currentAmountMinorUnits: Long,

    @Column(name = "target_date")
    var targetDate: LocalDate?,

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    var status: GoalStatusEntity,

    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: Instant,

    @Column(name = "updated_at", nullable = false)
    var updatedAt: Instant
)

enum class GoalStatusEntity { ACTIVE, COMPLETED, CANCELLED }
