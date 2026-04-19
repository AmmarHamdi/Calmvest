package com.calmvest.core.domain.usecase

import com.calmvest.core.domain.model.Portfolio
import com.calmvest.core.domain.repository.PortfolioRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPortfolioUseCase @Inject constructor(
    private val portfolioRepository: PortfolioRepository
) {
    operator fun invoke(userId: String): Flow<Result<Portfolio>> =
        portfolioRepository.getPortfolio(userId)
}
