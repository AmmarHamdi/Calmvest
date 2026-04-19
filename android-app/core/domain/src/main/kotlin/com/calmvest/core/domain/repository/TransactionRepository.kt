package com.calmvest.core.domain.repository

import com.calmvest.core.domain.model.Transaction
import kotlinx.coroutines.flow.Flow

interface TransactionRepository {
    fun getTransactions(userId: String): Flow<Result<List<Transaction>>>
    fun getTransactionsByBankAccount(userId: String, bankAccountId: String): Flow<Result<List<Transaction>>>
}
