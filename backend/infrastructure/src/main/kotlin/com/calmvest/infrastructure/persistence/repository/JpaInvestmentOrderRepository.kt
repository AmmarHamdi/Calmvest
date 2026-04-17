package com.calmvest.infrastructure.persistence.repository

import com.calmvest.infrastructure.persistence.entity.InvestmentOrderEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional
import java.util.UUID

interface JpaInvestmentOrderRepository : JpaRepository<InvestmentOrderEntity, UUID> {
    fun findByUserId(userId: UUID): List<InvestmentOrderEntity>
    fun findByIdempotencyKey(idempotencyKey: String): Optional<InvestmentOrderEntity>
    fun existsByIdempotencyKey(idempotencyKey: String): Boolean
}
