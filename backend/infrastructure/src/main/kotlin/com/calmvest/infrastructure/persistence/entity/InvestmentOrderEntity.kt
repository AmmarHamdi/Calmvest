package com.calmvest.infrastructure.persistence.entity

import jakarta.persistence.*
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "investment_orders")
class InvestmentOrderEntity(
    @Id
    @Column(name = "id", updatable = false, nullable = false)
    val id: UUID,

    @Column(name = "user_id", nullable = false)
    val userId: UUID,

    @Column(name = "goal_id", nullable = false)
    val goalId: UUID,

    @Column(name = "amount_minor_units", nullable = false)
    val amountMinorUnits: Long,

    @Column(name = "mode", nullable = false)
    @Enumerated(EnumType.STRING)
    var mode: InvestmentModeEntity,

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    var status: OrderStatusEntity,

    @Column(name = "idempotency_key", nullable = false, unique = true)
    val idempotencyKey: String,

    @Column(name = "provider_order_id")
    var providerOrderId: String?,

    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: Instant,

    @Column(name = "executed_at")
    var executedAt: Instant?,

    @Column(name = "updated_at", nullable = false)
    var updatedAt: Instant
)

enum class InvestmentModeEntity { SAFE, BITCOIN, DIVERSIFIED }

enum class OrderStatusEntity { PENDING, SUBMITTED, EXECUTED, FAILED, CANCELLED }
