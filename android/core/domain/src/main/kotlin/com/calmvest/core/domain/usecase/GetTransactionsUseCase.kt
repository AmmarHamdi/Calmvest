package com.calmvest.core.domain.usecase

import com.calmvest.core.domain.model.Transaction
import com.calmvest.core.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTransactionsUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository
) {
    operator fun invoke(userId: String): Flow<Result<List<Transaction>>> =
        transactionRepository.getTransactions(userId)
}
