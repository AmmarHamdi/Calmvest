package com.calmvest.core.domain.usecase

import com.calmvest.core.domain.model.Goal
import com.calmvest.core.domain.model.GoalType
import com.calmvest.core.domain.repository.GoalRepository
import javax.inject.Inject

class CreateGoalUseCase @Inject constructor(
    private val goalRepository: GoalRepository
) {
    suspend operator fun invoke(
        userId: String,
        name: String,
        goalType: GoalType,
        targetAmountMinorUnits: Long,
        targetDate: String?
    ): Result<Goal> = goalRepository.createGoal(userId, name, goalType, targetAmountMinorUnits, targetDate)
}
