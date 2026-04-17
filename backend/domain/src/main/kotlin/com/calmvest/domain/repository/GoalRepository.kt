package com.calmvest.domain.repository

import com.calmvest.domain.model.Goal
import com.calmvest.domain.model.GoalId
import com.calmvest.domain.model.GoalStatus
import com.calmvest.domain.model.UserId
import java.util.Optional

interface GoalRepository {
    fun save(goal: Goal): Goal
    fun findById(id: GoalId): Optional<Goal>
    fun findByUserId(userId: UserId): List<Goal>
    fun findByUserIdAndStatus(userId: UserId, status: GoalStatus): List<Goal>
}
