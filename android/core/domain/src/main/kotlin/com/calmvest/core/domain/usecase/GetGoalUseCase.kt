package com.calmvest.core.domain.usecase

import com.calmvest.core.domain.model.Goal
import com.calmvest.core.domain.repository.GoalRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetGoalUseCase @Inject constructor(
    private val goalRepository: GoalRepository
) {
    operator fun invoke(userId: String, goalId: String): Flow<Result<Goal>> =
        goalRepository.getGoal(userId, goalId)
}
