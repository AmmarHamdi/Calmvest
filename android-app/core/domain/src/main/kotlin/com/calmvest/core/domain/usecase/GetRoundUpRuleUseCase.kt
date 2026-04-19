package com.calmvest.core.domain.usecase

import com.calmvest.core.domain.model.RoundUpRule
import com.calmvest.core.domain.repository.RoundUpRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetRoundUpRuleUseCase @Inject constructor(
    private val roundUpRepository: RoundUpRepository
) {
    operator fun invoke(userId: String): Flow<Result<RoundUpRule>> =
        roundUpRepository.getRoundUpRule(userId)
}
