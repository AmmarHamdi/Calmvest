package com.calmvest.domain.model

import java.time.Instant

data class Transaction(
    val id: TransactionId,
    val bankAccountId: BankAccountId,
    val userId: UserId,
    val amount: Money,
    val roundUpAmount: Money,
    val description: String,
    val merchantName: String?,
    val transactedAt: Instant,
    val importedAt: Instant,
    val idempotencyKey: String
)
