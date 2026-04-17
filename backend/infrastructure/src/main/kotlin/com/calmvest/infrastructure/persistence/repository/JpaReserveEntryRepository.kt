package com.calmvest.infrastructure.persistence.repository

import com.calmvest.infrastructure.persistence.entity.ReserveEntryEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.util.Optional
import java.util.UUID

interface JpaReserveEntryRepository : JpaRepository<ReserveEntryEntity, UUID> {
    fun findByUserId(userId: UUID): List<ReserveEntryEntity>
    fun findByIdempotencyKey(idempotencyKey: String): Optional<ReserveEntryEntity>
    fun existsByIdempotencyKey(idempotencyKey: String): Boolean

    @Query("SELECT COALESCE(SUM(r.amountMinorUnits), 0) FROM ReserveEntryEntity r WHERE r.userId = :userId")
    fun sumAmountMinorUnitsByUserId(userId: UUID): Long
}
