package com.calmvest.core.domain.usecase

import com.calmvest.core.domain.model.RoundUpRule
import com.calmvest.core.domain.repository.RoundUpRepository
import javax.inject.Inject

class UpdateRoundUpRuleUseCase @Inject constructor(
    private val roundUpRepository: RoundUpRepository
) {
    suspend operator fun invoke(
        userId: String,
        isEnabled: Boolean,
        monthlyCapMinorUnits: Long,
        thresholdMinorUnits: Long,
        pauseUntil: String?
    ): Result<RoundUpRule> = roundUpRepository.updateRoundUpRule(
        userId, isEnabled, monthlyCapMinorUnits, thresholdMinorUnits, pauseUntil
    )
}
