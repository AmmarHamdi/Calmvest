package com.calmvest.domain.model

import java.time.Instant

data class BankAccount(
    val id: BankAccountId,
    val userId: UserId,
    val iban: String,
    val provider: String,
    val consentId: String,
    val isActive: Boolean,
    val createdAt: Instant
)
