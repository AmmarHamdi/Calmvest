package com.calmvest.infrastructure.persistence.repository

import com.calmvest.infrastructure.persistence.entity.RoundUpRuleEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional
import java.util.UUID

interface JpaRoundUpRuleRepository : JpaRepository<RoundUpRuleEntity, UUID> {
    fun findByUserId(userId: UUID): Optional<RoundUpRuleEntity>
}
