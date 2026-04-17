package com.calmvest.core.domain.repository

import com.calmvest.core.domain.model.RoundUpRule
import com.calmvest.core.domain.model.ReserveSummary
import kotlinx.coroutines.flow.Flow

interface RoundUpRepository {
    fun getRoundUpRule(userId: String): Flow<Result<RoundUpRule>>
    fun getReserveSummary(userId: String): Flow<Result<ReserveSummary>>
    suspend fun updateRoundUpRule(
        userId: String,
        isEnabled: Boolean,
        monthlyCapMinorUnits: Long,
        thresholdMinorUnits: Long,
        pauseUntil: String?
    ): Result<RoundUpRule>
}
