package com.calmvest.core.domain.usecase

import com.calmvest.core.domain.model.InvestmentOrder
import com.calmvest.core.domain.repository.PortfolioRepository
import javax.inject.Inject

class TriggerManualInvestmentUseCase @Inject constructor(
    private val portfolioRepository: PortfolioRepository
) {
    suspend operator fun invoke(userId: String): Result<InvestmentOrder> =
        portfolioRepository.triggerManualInvestment(userId)
}
