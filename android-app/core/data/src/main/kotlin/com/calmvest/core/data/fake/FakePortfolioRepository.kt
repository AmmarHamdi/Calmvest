package com.calmvest.core.data.fake

import com.calmvest.core.domain.model.InvestmentMode
import com.calmvest.core.domain.model.InvestmentOrder
import com.calmvest.core.domain.model.OrderStatus
import com.calmvest.core.domain.model.Portfolio
import com.calmvest.core.domain.repository.PortfolioRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FakePortfolioRepository @Inject constructor() : PortfolioRepository {

    private val fakePortfolio = Portfolio(
        id = "portfolio-1",
        userId = "demo-user-id",
        mode = InvestmentMode.SAFE,
        totalInvestedMinorUnits = 45_000,
        currentValueMinorUnits = 47_230
    )

    private val fakeOrders = listOf(
        InvestmentOrder(
            id = "order-1",
            userId = "demo-user-id",
            goalId = "goal-1",
            amountMinorUnits = 2_350,
            mode = InvestmentMode.SAFE,
            status = OrderStatus.EXECUTED,
            executedAt = "2024-04-10",
            createdAt = "2024-04-10"
        ),
        InvestmentOrder(
            id = "order-2",
            userId = "demo-user-id",
            goalId = "goal-2",
            amountMinorUnits = 1_870,
            mode = InvestmentMode.SAFE,
            status = OrderStatus.EXECUTED,
            executedAt = "2024-03-10",
            createdAt = "2024-03-10"
        )
    )

    override fun getPortfolio(userId: String): Flow<Result<Portfolio>> = flow {
        emit(Result.success(fakePortfolio))
    }

    override fun getInvestmentOrders(userId: String): Flow<Result<List<InvestmentOrder>>> = flow {
        emit(Result.success(fakeOrders))
    }

    override suspend fun triggerManualInvestment(userId: String): Result<InvestmentOrder> =
        Result.success(
            InvestmentOrder(
                id = "order-manual",
                userId = userId,
                goalId = null,
                amountMinorUnits = 2_800,
                mode = InvestmentMode.SAFE,
                status = OrderStatus.PENDING,
                executedAt = null,
                createdAt = "2024-04-17"
            )
        )

    override suspend fun updateInvestmentMode(userId: String, mode: InvestmentMode): Result<Portfolio> =
        Result.success(fakePortfolio.copy(mode = mode))
}
