package com.calmvest.core.domain.usecase

import com.calmvest.core.domain.model.Goal
import com.calmvest.core.domain.repository.GoalRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetGoalsUseCase @Inject constructor(
    private val goalRepository: GoalRepository
) {
    operator fun invoke(userId: String): Flow<Result<List<Goal>>> =
        goalRepository.getGoals(userId)
}
