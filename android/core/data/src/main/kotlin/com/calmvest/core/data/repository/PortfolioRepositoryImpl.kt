package com.calmvest.core.data.repository

import com.calmvest.core.data.remote.api.CalmvestApi
import com.calmvest.core.data.remote.dto.UpdateInvestmentModeRequest
import com.calmvest.core.data.remote.dto.toDomain
import com.calmvest.core.domain.model.InvestmentMode
import com.calmvest.core.domain.model.InvestmentOrder
import com.calmvest.core.domain.model.Portfolio
import com.calmvest.core.domain.repository.PortfolioRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class PortfolioRepositoryImpl @Inject constructor(
    private val api: CalmvestApi
) : PortfolioRepository {

    override fun getPortfolio(userId: String): Flow<Result<Portfolio>> = flow {
        emit(runCatching { api.getPortfolio(userId).toDomain() })
    }

    override fun getInvestmentOrders(userId: String): Flow<Result<List<InvestmentOrder>>> = flow {
        emit(runCatching { api.getInvestmentOrders(userId).map { it.toDomain() } })
    }

    override suspend fun triggerManualInvestment(userId: String): Result<InvestmentOrder> =
        runCatching { api.triggerManualInvestment(userId).toDomain() }

    override suspend fun updateInvestmentMode(userId: String, mode: InvestmentMode): Result<Portfolio> =
        runCatching {
            api.updateInvestmentMode(userId, UpdateInvestmentModeRequest(mode.name)).toDomain()
        }
}
