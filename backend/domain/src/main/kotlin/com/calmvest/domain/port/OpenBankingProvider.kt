package com.calmvest.domain.port

import com.calmvest.domain.model.BankAccountId
import com.calmvest.domain.model.Money
import java.time.Instant

/**
 * Port for interacting with an open banking provider to fetch account data.
 */
interface OpenBankingProvider {
    /**
     * Fetches the current balance for the given bank account.
     */
    fun getAccountBalance(consentId: String): Money

    /**
     * Validates that the open banking consent is still active.
     */
    fun isConsentActive(consentId: String): Boolean
}
