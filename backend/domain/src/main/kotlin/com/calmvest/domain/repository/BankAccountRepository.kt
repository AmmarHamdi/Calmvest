package com.calmvest.domain.repository

import com.calmvest.domain.model.BankAccount
import com.calmvest.domain.model.BankAccountId
import com.calmvest.domain.model.UserId
import java.util.Optional

interface BankAccountRepository {
    fun save(bankAccount: BankAccount): BankAccount
    fun findById(id: BankAccountId): Optional<BankAccount>
    fun findByUserId(userId: UserId): List<BankAccount>
    fun findActiveByUserId(userId: UserId): List<BankAccount>
}
