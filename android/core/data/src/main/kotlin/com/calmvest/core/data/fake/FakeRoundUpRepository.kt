package com.calmvest.core.data.fake

import com.calmvest.core.domain.model.ReserveSummary
import com.calmvest.core.domain.model.RoundUpRule
import com.calmvest.core.domain.repository.RoundUpRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FakeRoundUpRepository @Inject constructor() : RoundUpRepository {

    private val fakeRoundUpRule = RoundUpRule(
        id = "rule-1",
        userId = "demo-user-id",
        isEnabled = true,
        monthlyCapMinorUnits = 5_000,
        thresholdMinorUnits = 50,
        pauseUntil = null
    )

    private val fakeReserveSummary = ReserveSummary(
        userId = "demo-user-id",
        totalReserveMinorUnits = 25_000,
        pendingRoundUpsMinorUnits = 1_225,
        thisMonthRoundUpsMinorUnits = 2_800
    )

    override fun getRoundUpRule(userId: String): Flow<Result<RoundUpRule>> = flow {
        emit(Result.success(fakeRoundUpRule))
    }

    override fun getReserveSummary(userId: String): Flow<Result<ReserveSummary>> = flow {
        emit(Result.success(fakeReserveSummary))
    }

    override suspend fun updateRoundUpRule(
        userId: String,
        isEnabled: Boolean,
        monthlyCapMinorUnits: Long,
        thresholdMinorUnits: Long,
        pauseUntil: String?
    ): Result<RoundUpRule> = Result.success(
        fakeRoundUpRule.copy(
            isEnabled = isEnabled,
            monthlyCapMinorUnits = monthlyCapMinorUnits,
            thresholdMinorUnits = thresholdMinorUnits,
            pauseUntil = pauseUntil
        )
    )
}
