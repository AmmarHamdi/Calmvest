package com.calmvest.core.data.repository

import com.calmvest.core.data.remote.api.CalmvestApi
import com.calmvest.core.data.remote.dto.toDomain
import com.calmvest.core.domain.model.Transaction
import com.calmvest.core.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class TransactionRepositoryImpl @Inject constructor(
    private val api: CalmvestApi
) : TransactionRepository {

    override fun getTransactions(userId: String): Flow<Result<List<Transaction>>> = flow {
        emit(runCatching { api.getTransactions(userId).map { it.toDomain() } })
    }

    override fun getTransactionsByBankAccount(
        userId: String,
        bankAccountId: String
    ): Flow<Result<List<Transaction>>> = flow {
        emit(runCatching {
            api.getTransactionsByBankAccount(userId, bankAccountId).map { it.toDomain() }
        })
    }
}
