package com.calmvest.core.data.fake

import com.calmvest.core.domain.model.Money
import com.calmvest.core.domain.model.Transaction
import com.calmvest.core.domain.model.TransactionType
import com.calmvest.core.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FakeTransactionRepository @Inject constructor() : TransactionRepository {

    private val fakeTransactions = listOf(
        Transaction(
            id = "txn-1",
            userId = "demo-user-id",
            bankAccountId = "bank-1",
            merchantName = "Café Noir",
            amount = Money.ofMinorUnits(350),
            roundUpAmount = Money.ofMinorUnits(50),
            transactionType = TransactionType.DEBIT,
            date = "2024-04-17",
            isRoundedUp = true
        ),
        Transaction(
            id = "txn-2",
            userId = "demo-user-id",
            bankAccountId = "bank-1",
            merchantName = "Supermarkt Plus",
            amount = Money.ofMinorUnits(2847),
            roundUpAmount = Money.ofMinorUnits(53),
            transactionType = TransactionType.DEBIT,
            date = "2024-04-16",
            isRoundedUp = true
        ),
        Transaction(
            id = "txn-3",
            userId = "demo-user-id",
            bankAccountId = "bank-1",
            merchantName = "Metro Ticket",
            amount = Money.ofMinorUnits(280),
            roundUpAmount = Money.ofMinorUnits(20),
            transactionType = TransactionType.DEBIT,
            date = "2024-04-15",
            isRoundedUp = true
        ),
        Transaction(
            id = "txn-4",
            userId = "demo-user-id",
            bankAccountId = "bank-1",
            merchantName = "Salary",
            amount = Money.ofMinorUnits(280000),
            roundUpAmount = Money.zero(),
            transactionType = TransactionType.CREDIT,
            date = "2024-04-15",
            isRoundedUp = false
        ),
        Transaction(
            id = "txn-5",
            userId = "demo-user-id",
            bankAccountId = "bank-1",
            merchantName = "Gym Membership",
            amount = Money.ofMinorUnits(2900),
            roundUpAmount = Money.ofMinorUnits(100),
            transactionType = TransactionType.DEBIT,
            date = "2024-04-14",
            isRoundedUp = true
        ),
        Transaction(
            id = "txn-6",
            userId = "demo-user-id",
            bankAccountId = "bank-1",
            merchantName = "BookShop",
            amount = Money.ofMinorUnits(1499),
            roundUpAmount = Money.ofMinorUnits(1),
            transactionType = TransactionType.DEBIT,
            date = "2024-04-13",
            isRoundedUp = true
        ),
        Transaction(
            id = "txn-7",
            userId = "demo-user-id",
            bankAccountId = "bank-1",
            merchantName = "Online Streaming",
            amount = Money.ofMinorUnits(999),
            roundUpAmount = Money.ofMinorUnits(1),
            transactionType = TransactionType.DEBIT,
            date = "2024-04-12",
            isRoundedUp = true
        )
    )

    override fun getTransactions(userId: String): Flow<Result<List<Transaction>>> = flow {
        emit(Result.success(fakeTransactions))
    }

    override fun getTransactionsByBankAccount(
        userId: String,
        bankAccountId: String
    ): Flow<Result<List<Transaction>>> = flow {
        emit(Result.success(fakeTransactions.filter { it.bankAccountId == bankAccountId }))
    }
}
