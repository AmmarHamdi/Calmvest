package com.calmvest.core.domain.usecase

import com.calmvest.core.domain.model.ReserveSummary
import com.calmvest.core.domain.repository.RoundUpRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetReserveSummaryUseCase @Inject constructor(
    private val roundUpRepository: RoundUpRepository
) {
    operator fun invoke(userId: String): Flow<Result<ReserveSummary>> =
        roundUpRepository.getReserveSummary(userId)
}
