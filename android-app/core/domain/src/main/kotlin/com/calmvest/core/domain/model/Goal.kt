package com.calmvest.core.domain.model

enum class GoalType {
    TRAVEL, EMERGENCY_FUND, CAR, HOUSE, EDUCATION, OTHER
}

data class Goal(
    val id: String,
    val userId: String,
    val name: String,
    val goalType: GoalType,
    val targetAmount: Money,
    val currentAmount: Money,
    val targetDate: String?,
    val isActive: Boolean,
    val createdAt: String
) {
    val progressFraction: Float
        get() = if (targetAmount.minorUnits == 0L) 0f
                else (currentAmount.minorUnits.toFloat() / targetAmount.minorUnits.toFloat()).coerceIn(0f, 1f)

    val progressPercent: Int get() = (progressFraction * 100).toInt()

    val emoji: String get() = when (goalType) {
        GoalType.TRAVEL -> "✈️"
        GoalType.EMERGENCY_FUND -> "🛡️"
        GoalType.CAR -> "🚗"
        GoalType.HOUSE -> "🏠"
        GoalType.EDUCATION -> "📚"
        GoalType.OTHER -> "💫"
    }
}
