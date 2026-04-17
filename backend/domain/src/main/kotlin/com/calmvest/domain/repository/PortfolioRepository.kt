package com.calmvest.domain.repository

import com.calmvest.domain.model.InvestmentMode
import com.calmvest.domain.model.Portfolio
import com.calmvest.domain.model.PortfolioId
import com.calmvest.domain.model.UserId
import java.util.Optional

interface PortfolioRepository {
    fun save(portfolio: Portfolio): Portfolio
    fun findById(id: PortfolioId): Optional<Portfolio>
    fun findByUserId(userId: UserId): List<Portfolio>
    fun findByUserIdAndMode(userId: UserId, mode: InvestmentMode): Optional<Portfolio>
}
