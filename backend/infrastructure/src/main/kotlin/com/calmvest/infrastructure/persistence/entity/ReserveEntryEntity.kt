package com.calmvest.infrastructure.persistence.entity

import jakarta.persistence.*
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "reserve_entries")
class ReserveEntryEntity(
    @Id
    @Column(name = "id", updatable = false, nullable = false)
    val id: UUID,

    @Column(name = "user_id", nullable = false)
    val userId: UUID,

    @Column(name = "transaction_id", nullable = false)
    val transactionId: UUID,

    @Column(name = "amount_minor_units", nullable = false)
    val amountMinorUnits: Long,

    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: Instant,

    @Column(name = "idempotency_key", nullable = false, unique = true)
    val idempotencyKey: String
)
