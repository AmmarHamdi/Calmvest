package com.calmvest.infrastructure.persistence.entity

import jakarta.persistence.*
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "transactions")
class TransactionEntity(
    @Id
    @Column(name = "id", updatable = false, nullable = false)
    val id: UUID,

    @Column(name = "bank_account_id", nullable = false)
    val bankAccountId: UUID,

    @Column(name = "user_id", nullable = false)
    val userId: UUID,

    @Column(name = "amount_minor_units", nullable = false)
    var amountMinorUnits: Long,

    @Column(name = "round_up_amount_minor_units", nullable = false)
    var roundUpAmountMinorUnits: Long,

    @Column(name = "description", nullable = false)
    var description: String,

    @Column(name = "merchant_name")
    var merchantName: String?,

    @Column(name = "transacted_at", nullable = false)
    var transactedAt: Instant,

    @Column(name = "imported_at", nullable = false)
    val importedAt: Instant,

    @Column(name = "idempotency_key", nullable = false, unique = true)
    val idempotencyKey: String
)
