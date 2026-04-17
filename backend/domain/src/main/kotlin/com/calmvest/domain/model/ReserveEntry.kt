package com.calmvest.domain.model

import java.time.Instant

data class ReserveEntry(
    val id: ReserveEntryId,
    val userId: UserId,
    val transactionId: TransactionId,
    val amount: Money,
    val createdAt: Instant,
    val idempotencyKey: String
)
