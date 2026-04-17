package com.calmvest.domain.model

import java.util.UUID

@JvmInline value class UserId(val value: UUID) {
    companion object { fun generate() = UserId(UUID.randomUUID()) }
    override fun toString() = value.toString()
}

@JvmInline value class BankAccountId(val value: UUID) {
    companion object { fun generate() = BankAccountId(UUID.randomUUID()) }
    override fun toString() = value.toString()
}

@JvmInline value class TransactionId(val value: UUID) {
    companion object { fun generate() = TransactionId(UUID.randomUUID()) }
    override fun toString() = value.toString()
}

@JvmInline value class RoundUpRuleId(val value: UUID) {
    companion object { fun generate() = RoundUpRuleId(UUID.randomUUID()) }
    override fun toString() = value.toString()
}

@JvmInline value class ReserveEntryId(val value: UUID) {
    companion object { fun generate() = ReserveEntryId(UUID.randomUUID()) }
    override fun toString() = value.toString()
}

@JvmInline value class GoalId(val value: UUID) {
    companion object { fun generate() = GoalId(UUID.randomUUID()) }
    override fun toString() = value.toString()
}

@JvmInline value class InvestmentOrderId(val value: UUID) {
    companion object { fun generate() = InvestmentOrderId(UUID.randomUUID()) }
    override fun toString() = value.toString()
}

@JvmInline value class PortfolioId(val value: UUID) {
    companion object { fun generate() = PortfolioId(UUID.randomUUID()) }
    override fun toString() = value.toString()
}
