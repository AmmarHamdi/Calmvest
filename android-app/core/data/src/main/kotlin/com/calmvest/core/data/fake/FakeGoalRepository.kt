package com.calmvest.core.data.fake

import com.calmvest.core.domain.model.GoalType
import com.calmvest.core.domain.model.Money
import com.calmvest.core.domain.model.Goal
import com.calmvest.core.domain.repository.GoalRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FakeGoalRepository @Inject constructor() : GoalRepository {

    private val fakeGoals = listOf(
        Goal(
            id = "goal-1",
            userId = "demo-user-id",
            name = "Emergency Fund",
            goalType = GoalType.EMERGENCY_FUND,
            targetAmount = Money.ofMinorUnits(200_000),
            currentAmount = Money.ofMinorUnits(82_000),
            targetDate = "2025-12-31",
            isActive = true,
            createdAt = "2024-01-15"
        ),
        Goal(
            id = "goal-2",
            userId = "demo-user-id",
            name = "Japan Trip",
            goalType = GoalType.TRAVEL,
            targetAmount = Money.ofMinorUnits(300_000),
            currentAmount = Money.ofMinorUnits(120_000),
            targetDate = "2025-08-01",
            isActive = true,
            createdAt = "2024-02-10"
        ),
        Goal(
            id = "goal-3",
            userId = "demo-user-id",
            name = "New Laptop",
            goalType = GoalType.OTHER,
            targetAmount = Money.ofMinorUnits(150_000),
            currentAmount = Money.ofMinorUnits(150_000),
            targetDate = null,
            isActive = false,
            createdAt = "2023-11-01"
        )
    )

    override fun getGoals(userId: String): Flow<Result<List<Goal>>> = flow {
        emit(Result.success(fakeGoals))
    }

    override fun getGoal(userId: String, goalId: String): Flow<Result<Goal>> = flow {
        val goal = fakeGoals.find { it.id == goalId }
        if (goal != null) emit(Result.success(goal))
        else emit(Result.failure(NoSuchElementException("Goal $goalId not found")))
    }

    override suspend fun createGoal(
        userId: String,
        name: String,
        goalType: GoalType,
        targetAmountMinorUnits: Long,
        targetDate: String?
    ): Result<Goal> = Result.success(
        Goal(
            id = "goal-new",
            userId = userId,
            name = name,
            goalType = goalType,
            targetAmount = Money.ofMinorUnits(targetAmountMinorUnits),
            currentAmount = Money.zero(),
            targetDate = targetDate,
            isActive = true,
            createdAt = "2024-04-17"
        )
    )

    override suspend fun updateGoal(userId: String, goalId: String, isActive: Boolean): Result<Goal> {
        val goal = fakeGoals.find { it.id == goalId }
            ?: return Result.failure(NoSuchElementException("Goal $goalId not found"))
        return Result.success(goal.copy(isActive = isActive))
    }

    override suspend fun deleteGoal(userId: String, goalId: String): Result<Unit> =
        Result.success(Unit)
}
