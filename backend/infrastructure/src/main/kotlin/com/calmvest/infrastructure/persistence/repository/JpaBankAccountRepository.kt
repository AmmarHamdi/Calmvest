package com.calmvest.infrastructure.persistence.repository

import com.calmvest.infrastructure.persistence.entity.BankAccountEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface JpaBankAccountRepository : JpaRepository<BankAccountEntity, UUID> {
    fun findByUserId(userId: UUID): List<BankAccountEntity>
    fun findByUserIdAndIsActive(userId: UUID, isActive: Boolean): List<BankAccountEntity>
}
