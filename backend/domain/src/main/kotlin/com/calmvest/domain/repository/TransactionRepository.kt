package com.calmvest.domain.repository

import com.calmvest.domain.model.Transaction
import com.calmvest.domain.model.TransactionId
import com.calmvest.domain.model.UserId
import java.util.Optional

interface TransactionRepository {
    fun save(transaction: Transaction): Transaction
    fun findById(id: TransactionId): Optional<Transaction>
    fun findByUserId(userId: UserId): List<Transaction>
    fun findByIdempotencyKey(idempotencyKey: String): Optional<Transaction>
    fun existsByIdempotencyKey(idempotencyKey: String): Boolean
}
