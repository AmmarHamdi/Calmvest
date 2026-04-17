package com.calmvest.infrastructure.persistence.repository

import com.calmvest.infrastructure.persistence.entity.InvestmentModeEntity
import com.calmvest.infrastructure.persistence.entity.PortfolioEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional
import java.util.UUID

interface JpaPortfolioRepository : JpaRepository<PortfolioEntity, UUID> {
    fun findByUserId(userId: UUID): List<PortfolioEntity>
    fun findByUserIdAndMode(userId: UUID, mode: InvestmentModeEntity): Optional<PortfolioEntity>
}
