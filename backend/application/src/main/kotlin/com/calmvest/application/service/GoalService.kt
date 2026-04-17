package com.calmvest.application.service

import com.calmvest.application.dto.CreateGoalRequest
import com.calmvest.application.dto.GoalDto
import com.calmvest.application.dto.UpdateGoalRequest
import com.calmvest.domain.model.Goal
import com.calmvest.domain.model.GoalId
import com.calmvest.domain.model.GoalStatus
import com.calmvest.domain.model.Money
import com.calmvest.domain.model.UserId
import com.calmvest.domain.repository.GoalRepository
import com.calmvest.domain.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.time.LocalDate
import java.util.UUID

@Service
class GoalService(
    private val goalRepository: GoalRepository,
    private val userRepository: UserRepository
) {

    @Transactional
    fun createGoal(userId: UUID, request: CreateGoalRequest): GoalDto {
        require(request.name.isNotBlank()) { "Goal name must not be blank" }
        require(request.targetAmountEuros > java.math.BigDecimal.ZERO) {
            "Target amount must be positive"
        }

        val uid = UserId(userId)
        userRepository.findById(uid)
            .orElseThrow { NoSuchElementException("User not found: $userId") }

        val now = Instant.now()
        val goal = Goal(
            id = GoalId.generate(),
            userId = uid,
            name = request.name.trim(),
            description = request.description?.trim(),
            targetAmount = Money.ofEuros(request.targetAmountEuros),
            currentAmount = Money.zero(),
            targetDate = request.targetDate?.let { LocalDate.parse(it) },
            status = GoalStatus.ACTIVE,
            createdAt = now,
            updatedAt = now
        )
        return goalRepository.save(goal).toDto()
    }

    @Transactional
    fun updateGoal(userId: UUID, goalId: UUID, request: UpdateGoalRequest): GoalDto {
        val uid = UserId(userId)
        val gid = GoalId(goalId)

        val existing = goalRepository.findById(gid)
            .orElseThrow { NoSuchElementException("Goal not found: $goalId") }
        require(existing.userId == uid) { "Goal does not belong to user $userId" }

        val updated = existing.copy(
            name = request.name?.trim() ?: existing.name,
            description = request.description?.trim() ?: existing.description,
            targetAmount = request.targetAmountEuros?.let { Money.ofEuros(it) } ?: existing.targetAmount,
            targetDate = request.targetDate?.let { LocalDate.parse(it) } ?: existing.targetDate,
            status = request.status?.let { GoalStatus.valueOf(it.uppercase()) } ?: existing.status,
            updatedAt = Instant.now()
        )
        return goalRepository.save(updated).toDto()
    }

    @Transactional(readOnly = true)
    fun getGoalsForUser(userId: UUID): List<GoalDto> {
        val uid = UserId(userId)
        userRepository.findById(uid)
            .orElseThrow { NoSuchElementException("User not found: $userId") }
        return goalRepository.findByUserId(uid).map { it.toDto() }
    }

    private fun Goal.toDto() = GoalDto(
        id = id.value,
        userId = userId.value,
        name = name,
        description = description,
        targetAmountEuros = targetAmount.toEuros(),
        currentAmountEuros = currentAmount.toEuros(),
        targetDate = targetDate?.toString(),
        status = status.name,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}
