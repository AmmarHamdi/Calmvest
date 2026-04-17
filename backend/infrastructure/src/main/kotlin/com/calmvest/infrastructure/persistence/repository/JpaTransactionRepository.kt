package com.calmvest.infrastructure.persistence.repository

import com.calmvest.infrastructure.persistence.entity.TransactionEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional
import java.util.UUID

interface JpaTransactionRepository : JpaRepository<TransactionEntity, UUID> {
    fun findByUserId(userId: UUID): List<TransactionEntity>
    fun findByIdempotencyKey(idempotencyKey: String): Optional<TransactionEntity>
    fun existsByIdempotencyKey(idempotencyKey: String): Boolean
}
