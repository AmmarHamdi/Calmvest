package com.calmvest.core.domain.model

enum class TransactionType {
    DEBIT, CREDIT
}

data class Transaction(
    val id: String,
    val userId: String,
    val bankAccountId: String,
    val merchantName: String,
    val amount: Money,
    val roundUpAmount: Money,
    val transactionType: TransactionType,
    val date: String,
    val isRoundedUp: Boolean
)
