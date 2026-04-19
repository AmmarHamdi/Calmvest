package com.calmvest.core.domain.repository

import com.calmvest.core.domain.model.Portfolio
import com.calmvest.core.domain.model.InvestmentOrder
import com.calmvest.core.domain.model.InvestmentMode
import kotlinx.coroutines.flow.Flow

interface PortfolioRepository {
    fun getPortfolio(userId: String): Flow<Result<Portfolio>>
    fun getInvestmentOrders(userId: String): Flow<Result<List<InvestmentOrder>>>
    suspend fun triggerManualInvestment(userId: String): Result<InvestmentOrder>
    suspend fun updateInvestmentMode(userId: String, mode: InvestmentMode): Result<Portfolio>
}
