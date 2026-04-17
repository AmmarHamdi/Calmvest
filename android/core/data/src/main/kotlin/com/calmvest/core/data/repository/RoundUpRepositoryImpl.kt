package com.calmvest.core.data.repository

import com.calmvest.core.data.remote.api.CalmvestApi
import com.calmvest.core.data.remote.dto.UpdateRoundUpRuleRequest
import com.calmvest.core.data.remote.dto.toDomain
import com.calmvest.core.domain.model.ReserveSummary
import com.calmvest.core.domain.model.RoundUpRule
import com.calmvest.core.domain.repository.RoundUpRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RoundUpRepositoryImpl @Inject constructor(
    private val api: CalmvestApi
) : RoundUpRepository {

    override fun getRoundUpRule(userId: String): Flow<Result<RoundUpRule>> = flow {
        emit(runCatching { api.getRoundUpRule(userId).toDomain() })
    }

    override fun getReserveSummary(userId: String): Flow<Result<ReserveSummary>> = flow {
        emit(runCatching { api.getReserveSummary(userId).toDomain() })
    }

    override suspend fun updateRoundUpRule(
        userId: String,
        isEnabled: Boolean,
        monthlyCapMinorUnits: Long,
        thresholdMinorUnits: Long,
        pauseUntil: String?
    ): Result<RoundUpRule> = runCatching {
        api.updateRoundUpRule(
            userId,
            UpdateRoundUpRuleRequest(
                isEnabled = isEnabled,
                monthlyCapMinorUnits = monthlyCapMinorUnits,
                thresholdMinorUnits = thresholdMinorUnits,
                pauseUntil = pauseUntil
            )
        ).toDomain()
    }
}
