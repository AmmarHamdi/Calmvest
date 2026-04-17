package com.calmvest.infrastructure.persistence.repository

import com.calmvest.infrastructure.persistence.entity.GoalEntity
import com.calmvest.infrastructure.persistence.entity.GoalStatusEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface JpaGoalRepository : JpaRepository<GoalEntity, UUID> {
    fun findByUserId(userId: UUID): List<GoalEntity>
    fun findByUserIdAndStatus(userId: UUID, status: GoalStatusEntity): List<GoalEntity>
}
