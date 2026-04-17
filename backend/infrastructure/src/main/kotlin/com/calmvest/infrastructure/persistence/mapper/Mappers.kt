package com.calmvest.infrastructure.persistence.mapper

import com.calmvest.domain.model.*
import com.calmvest.infrastructure.persistence.entity.*

object UserMapper {
    fun toDomain(entity: UserEntity) = User(
        id = UserId(entity.id),
        email = entity.email,
        name = entity.name,
        kycStatus = KycStatus.valueOf(entity.kycStatus.name),
        createdAt = entity.createdAt,
        updatedAt = entity.updatedAt
    )

    fun toEntity(domain: User) = UserEntity(
        id = domain.id.value,
        email = domain.email,
        name = domain.name,
        kycStatus = KycStatusEntity.valueOf(domain.kycStatus.name),
        createdAt = domain.createdAt,
        updatedAt = domain.updatedAt
    )
}

object BankAccountMapper {
    fun toDomain(entity: BankAccountEntity) = BankAccount(
        id = BankAccountId(entity.id),
        userId = UserId(entity.userId),
        iban = entity.iban,
        provider = entity.provider,
        consentId = entity.consentId,
        isActive = entity.isActive,
        createdAt = entity.createdAt
    )

    fun toEntity(domain: BankAccount) = BankAccountEntity(
        id = domain.id.value,
        userId = domain.userId.value,
        iban = domain.iban,
        provider = domain.provider,
        consentId = domain.consentId,
        isActive = domain.isActive,
        createdAt = domain.createdAt
    )
}

object TransactionMapper {
    fun toDomain(entity: TransactionEntity) = Transaction(
        id = TransactionId(entity.id),
        bankAccountId = BankAccountId(entity.bankAccountId),
        userId = UserId(entity.userId),
        amount = Money(entity.amountMinorUnits),
        roundUpAmount = Money(entity.roundUpAmountMinorUnits),
        description = entity.description,
        merchantName = entity.merchantName,
        transactedAt = entity.transactedAt,
        importedAt = entity.importedAt,
        idempotencyKey = entity.idempotencyKey
    )

    fun toEntity(domain: Transaction) = TransactionEntity(
        id = domain.id.value,
        bankAccountId = domain.bankAccountId.value,
        userId = domain.userId.value,
        amountMinorUnits = domain.amount.minorUnits,
        roundUpAmountMinorUnits = domain.roundUpAmount.minorUnits,
        description = domain.description,
        merchantName = domain.merchantName,
        transactedAt = domain.transactedAt,
        importedAt = domain.importedAt,
        idempotencyKey = domain.idempotencyKey
    )
}

object RoundUpRuleMapper {
    fun toDomain(entity: RoundUpRuleEntity) = RoundUpRule(
        id = RoundUpRuleId(entity.id),
        userId = UserId(entity.userId),
        isEnabled = entity.isEnabled,
        monthlyCapAmount = Money(entity.monthlyCapMinorUnits),
        currentMonthAccumulated = Money(entity.currentMonthAccumulatedMinorUnits),
        pausedUntil = entity.pausedUntil,
        investmentThreshold = Money(entity.investmentThresholdMinorUnits),
        createdAt = entity.createdAt,
        updatedAt = entity.updatedAt
    )

    fun toEntity(domain: RoundUpRule) = RoundUpRuleEntity(
        id = domain.id.value,
        userId = domain.userId.value,
        isEnabled = domain.isEnabled,
        monthlyCapMinorUnits = domain.monthlyCapAmount.minorUnits,
        currentMonthAccumulatedMinorUnits = domain.currentMonthAccumulated.minorUnits,
        pausedUntil = domain.pausedUntil,
        investmentThresholdMinorUnits = domain.investmentThreshold.minorUnits,
        createdAt = domain.createdAt,
        updatedAt = domain.updatedAt
    )
}

object ReserveEntryMapper {
    fun toDomain(entity: ReserveEntryEntity) = ReserveEntry(
        id = ReserveEntryId(entity.id),
        userId = UserId(entity.userId),
        transactionId = TransactionId(entity.transactionId),
        amount = Money(entity.amountMinorUnits),
        createdAt = entity.createdAt,
        idempotencyKey = entity.idempotencyKey
    )

    fun toEntity(domain: ReserveEntry) = ReserveEntryEntity(
        id = domain.id.value,
        userId = domain.userId.value,
        transactionId = domain.transactionId.value,
        amountMinorUnits = domain.amount.minorUnits,
        createdAt = domain.createdAt,
        idempotencyKey = domain.idempotencyKey
    )
}

object GoalMapper {
    fun toDomain(entity: GoalEntity) = Goal(
        id = GoalId(entity.id),
        userId = UserId(entity.userId),
        name = entity.name,
        description = entity.description,
        targetAmount = Money(entity.targetAmountMinorUnits),
        currentAmount = Money(entity.currentAmountMinorUnits),
        targetDate = entity.targetDate,
        status = GoalStatus.valueOf(entity.status.name),
        createdAt = entity.createdAt,
        updatedAt = entity.updatedAt
    )

    fun toEntity(domain: Goal) = GoalEntity(
        id = domain.id.value,
        userId = domain.userId.value,
        name = domain.name,
        description = domain.description,
        targetAmountMinorUnits = domain.targetAmount.minorUnits,
        currentAmountMinorUnits = domain.currentAmount.minorUnits,
        targetDate = domain.targetDate,
        status = GoalStatusEntity.valueOf(domain.status.name),
        createdAt = domain.createdAt,
        updatedAt = domain.updatedAt
    )
}

object InvestmentOrderMapper {
    fun toDomain(entity: InvestmentOrderEntity) = InvestmentOrder(
        id = InvestmentOrderId(entity.id),
        userId = UserId(entity.userId),
        goalId = GoalId(entity.goalId),
        amount = Money(entity.amountMinorUnits),
        mode = InvestmentMode.valueOf(entity.mode.name),
        status = OrderStatus.valueOf(entity.status.name),
        idempotencyKey = entity.idempotencyKey,
        providerOrderId = entity.providerOrderId,
        createdAt = entity.createdAt,
        executedAt = entity.executedAt,
        updatedAt = entity.updatedAt
    )

    fun toEntity(domain: InvestmentOrder) = InvestmentOrderEntity(
        id = domain.id.value,
        userId = domain.userId.value,
        goalId = domain.goalId.value,
        amountMinorUnits = domain.amount.minorUnits,
        mode = InvestmentModeEntity.valueOf(domain.mode.name),
        status = OrderStatusEntity.valueOf(domain.status.name),
        idempotencyKey = domain.idempotencyKey,
        providerOrderId = domain.providerOrderId,
        createdAt = domain.createdAt,
        executedAt = domain.executedAt,
        updatedAt = domain.updatedAt
    )
}

object PortfolioMapper {
    fun toDomain(entity: PortfolioEntity) = Portfolio(
        id = PortfolioId(entity.id),
        userId = UserId(entity.userId),
        mode = InvestmentMode.valueOf(entity.mode.name),
        totalInvested = Money(entity.totalInvestedMinorUnits),
        currentValue = Money(entity.currentValueMinorUnits),
        createdAt = entity.createdAt,
        updatedAt = entity.updatedAt
    )

    fun toEntity(domain: Portfolio) = PortfolioEntity(
        id = domain.id.value,
        userId = domain.userId.value,
        mode = InvestmentModeEntity.valueOf(domain.mode.name),
        totalInvestedMinorUnits = domain.totalInvested.minorUnits,
        currentValueMinorUnits = domain.currentValue.minorUnits,
        createdAt = domain.createdAt,
        updatedAt = domain.updatedAt
    )
}
