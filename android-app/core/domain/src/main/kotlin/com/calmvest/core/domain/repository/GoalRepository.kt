package com.calmvest.core.domain.repository

import com.calmvest.core.domain.model.Goal
import com.calmvest.core.domain.model.GoalType
import kotlinx.coroutines.flow.Flow

interface GoalRepository {
    fun getGoals(userId: String): Flow<Result<List<Goal>>>
    fun getGoal(userId: String, goalId: String): Flow<Result<Goal>>
    suspend fun createGoal(
        userId: String,
        name: String,
        goalType: GoalType,
        targetAmountMinorUnits: Long,
        targetDate: String?
    ): Result<Goal>
    suspend fun updateGoal(userId: String, goalId: String, isActive: Boolean): Result<Goal>
    suspend fun deleteGoal(userId: String, goalId: String): Result<Unit>
}
