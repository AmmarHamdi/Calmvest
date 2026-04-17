package com.calmvest.infrastructure.persistence

import com.calmvest.domain.model.*
import com.calmvest.domain.repository.*
import com.calmvest.infrastructure.persistence.mapper.*
import com.calmvest.infrastructure.persistence.repository.*
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
class UserRepositoryAdapter(
    private val jpaRepo: JpaUserRepository
) : UserRepository {
    override fun save(user: User) = UserMapper.toDomain(jpaRepo.save(UserMapper.toEntity(user)))
    override fun findById(id: UserId) = jpaRepo.findById(id.value).map { UserMapper.toDomain(it) }
    override fun findByEmail(email: String) = jpaRepo.findByEmail(email).map { UserMapper.toDomain(it) }
    override fun findAll() = jpaRepo.findAll().map { UserMapper.toDomain(it) }
    override fun existsByEmail(email: String) = jpaRepo.existsByEmail(email)
}

@Repository
class BankAccountRepositoryAdapter(
    private val jpaRepo: JpaBankAccountRepository
) : BankAccountRepository {
    override fun save(bankAccount: BankAccount) =
        BankAccountMapper.toDomain(jpaRepo.save(BankAccountMapper.toEntity(bankAccount)))
    override fun findById(id: BankAccountId) =
        jpaRepo.findById(id.value).map { BankAccountMapper.toDomain(it) }
    override fun findByUserId(userId: UserId) =
        jpaRepo.findByUserId(userId.value).map { BankAccountMapper.toDomain(it) }
    override fun findActiveByUserId(userId: UserId) =
        jpaRepo.findByUserIdAndIsActive(userId.value, true).map { BankAccountMapper.toDomain(it) }
}

@Repository
class TransactionRepositoryAdapter(
    private val jpaRepo: JpaTransactionRepository
) : TransactionRepository {
    override fun save(transaction: Transaction) =
        TransactionMapper.toDomain(jpaRepo.save(TransactionMapper.toEntity(transaction)))
    override fun findById(id: TransactionId) =
        jpaRepo.findById(id.value).map { TransactionMapper.toDomain(it) }
    override fun findByUserId(userId: UserId) =
        jpaRepo.findByUserId(userId.value).map { TransactionMapper.toDomain(it) }
    override fun findByIdempotencyKey(idempotencyKey: String) =
        jpaRepo.findByIdempotencyKey(idempotencyKey).map { TransactionMapper.toDomain(it) }
    override fun existsByIdempotencyKey(idempotencyKey: String) =
        jpaRepo.existsByIdempotencyKey(idempotencyKey)
}

@Repository
class RoundUpRuleRepositoryAdapter(
    private val jpaRepo: JpaRoundUpRuleRepository
) : RoundUpRuleRepository {
    override fun save(rule: RoundUpRule) =
        RoundUpRuleMapper.toDomain(jpaRepo.save(RoundUpRuleMapper.toEntity(rule)))
    override fun findById(id: RoundUpRuleId) =
        jpaRepo.findById(id.value).map { RoundUpRuleMapper.toDomain(it) }
    override fun findByUserId(userId: UserId) =
        jpaRepo.findByUserId(userId.value).map { RoundUpRuleMapper.toDomain(it) }
}

@Repository
class ReserveEntryRepositoryAdapter(
    private val jpaRepo: JpaReserveEntryRepository
) : ReserveEntryRepository {
    override fun save(entry: ReserveEntry) =
        ReserveEntryMapper.toDomain(jpaRepo.save(ReserveEntryMapper.toEntity(entry)))
    override fun findById(id: ReserveEntryId) =
        jpaRepo.findById(id.value).map { ReserveEntryMapper.toDomain(it) }
    override fun findByUserId(userId: UserId) =
        jpaRepo.findByUserId(userId.value).map { ReserveEntryMapper.toDomain(it) }
    override fun findByIdempotencyKey(idempotencyKey: String) =
        jpaRepo.findByIdempotencyKey(idempotencyKey).map { ReserveEntryMapper.toDomain(it) }
    override fun existsByIdempotencyKey(idempotencyKey: String) =
        jpaRepo.existsByIdempotencyKey(idempotencyKey)
    override fun sumAmountByUserId(userId: UserId) =
        Money(jpaRepo.sumAmountMinorUnitsByUserId(userId.value))
}

@Repository
class GoalRepositoryAdapter(
    private val jpaRepo: JpaGoalRepository
) : GoalRepository {
    override fun save(goal: Goal) =
        GoalMapper.toDomain(jpaRepo.save(GoalMapper.toEntity(goal)))
    override fun findById(id: GoalId) =
        jpaRepo.findById(id.value).map { GoalMapper.toDomain(it) }
    override fun findByUserId(userId: UserId) =
        jpaRepo.findByUserId(userId.value).map { GoalMapper.toDomain(it) }
    override fun findByUserIdAndStatus(userId: UserId, status: GoalStatus) =
        jpaRepo.findByUserIdAndStatus(
            userId.value,
            com.calmvest.infrastructure.persistence.entity.GoalStatusEntity.valueOf(status.name)
        ).map { GoalMapper.toDomain(it) }
}

@Repository
class InvestmentOrderRepositoryAdapter(
    private val jpaRepo: JpaInvestmentOrderRepository
) : InvestmentOrderRepository {
    override fun save(order: InvestmentOrder) =
        InvestmentOrderMapper.toDomain(jpaRepo.save(InvestmentOrderMapper.toEntity(order)))
    override fun findById(id: InvestmentOrderId) =
        jpaRepo.findById(id.value).map { InvestmentOrderMapper.toDomain(it) }
    override fun findByUserId(userId: UserId) =
        jpaRepo.findByUserId(userId.value).map { InvestmentOrderMapper.toDomain(it) }
    override fun findByIdempotencyKey(idempotencyKey: String) =
        jpaRepo.findByIdempotencyKey(idempotencyKey).map { InvestmentOrderMapper.toDomain(it) }
    override fun existsByIdempotencyKey(idempotencyKey: String) =
        jpaRepo.existsByIdempotencyKey(idempotencyKey)
}

@Repository
class PortfolioRepositoryAdapter(
    private val jpaRepo: JpaPortfolioRepository
) : PortfolioRepository {
    override fun save(portfolio: Portfolio) =
        PortfolioMapper.toDomain(jpaRepo.save(PortfolioMapper.toEntity(portfolio)))
    override fun findById(id: PortfolioId) =
        jpaRepo.findById(id.value).map { PortfolioMapper.toDomain(it) }
    override fun findByUserId(userId: UserId) =
        jpaRepo.findByUserId(userId.value).map { PortfolioMapper.toDomain(it) }
    override fun findByUserIdAndMode(userId: UserId, mode: InvestmentMode) =
        jpaRepo.findByUserIdAndMode(
            userId.value,
            com.calmvest.infrastructure.persistence.entity.InvestmentModeEntity.valueOf(mode.name)
        ).map { PortfolioMapper.toDomain(it) }
}
