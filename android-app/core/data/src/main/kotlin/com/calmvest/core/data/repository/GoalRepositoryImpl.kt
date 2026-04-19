package com.calmvest.core.data.repository

import com.calmvest.core.data.remote.api.CalmvestApi
import com.calmvest.core.data.remote.dto.CreateGoalRequest
import com.calmvest.core.data.remote.dto.toDomain
import com.calmvest.core.domain.model.Goal
import com.calmvest.core.domain.model.GoalType
import com.calmvest.core.domain.repository.GoalRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GoalRepositoryImpl @Inject constructor(
    private val api: CalmvestApi
) : GoalRepository {

    override fun getGoals(userId: String): Flow<Result<List<Goal>>> = flow {
        emit(runCatching { api.getGoals(userId).map { it.toDomain() } })
    }

    override fun getGoal(userId: String, goalId: String): Flow<Result<Goal>> = flow {
        emit(runCatching { api.getGoal(userId, goalId).toDomain() })
    }

    override suspend fun createGoal(
        userId: String,
        name: String,
        goalType: GoalType,
        targetAmountMinorUnits: Long,
        targetDate: String?
    ): Result<Goal> = runCatching {
        api.createGoal(
            userId,
            CreateGoalRequest(
                name = name,
                goalType = goalType.name,
                targetAmountMinorUnits = targetAmountMinorUnits,
                targetDate = targetDate
            )
        ).toDomain()
    }

    override suspend fun updateGoal(userId: String, goalId: String, isActive: Boolean): Result<Goal> =
        runCatching {
            api.updateGoal(userId, goalId, mapOf("is_active" to isActive)).toDomain()
        }

    override suspend fun deleteGoal(userId: String, goalId: String): Result<Unit> =
        runCatching { api.deleteGoal(userId, goalId) }
}
