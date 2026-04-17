package com.calmvest.infrastructure.persistence.entity

import jakarta.persistence.*
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "bank_accounts")
class BankAccountEntity(
    @Id
    @Column(name = "id", updatable = false, nullable = false)
    val id: UUID,

    @Column(name = "user_id", nullable = false)
    val userId: UUID,

    @Column(name = "iban", nullable = false)
    var iban: String,

    @Column(name = "provider", nullable = false)
    var provider: String,

    @Column(name = "consent_id", nullable = false)
    var consentId: String,

    @Column(name = "is_active", nullable = false)
    var isActive: Boolean,

    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: Instant
)
