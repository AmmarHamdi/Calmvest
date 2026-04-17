package com.calmvest.domain.model

import java.time.Instant
import java.time.LocalDate

data class Goal(
    val id: GoalId,
    val userId: UserId,
    val name: String,
    val description: String?,
    val targetAmount: Money,
    val currentAmount: Money,
    val targetDate: LocalDate?,
    val status: GoalStatus,
    val createdAt: Instant,
    val updatedAt: Instant
)

enum class GoalStatus { ACTIVE, COMPLETED, CANCELLED }
