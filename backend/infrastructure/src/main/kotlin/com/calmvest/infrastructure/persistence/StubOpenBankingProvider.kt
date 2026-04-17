package com.calmvest.infrastructure.persistence

import com.calmvest.domain.model.Money
import com.calmvest.domain.port.OpenBankingProvider
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

/**
 * Stub implementation of OpenBankingProvider.
 * Replace this with a real open banking API integration (e.g. TrueLayer, Plaid).
 */
@Component
class StubOpenBankingProvider : OpenBankingProvider {

    private val log = LoggerFactory.getLogger(StubOpenBankingProvider::class.java)

    override fun getAccountBalance(consentId: String): Money {
        // TODO: integrate with real open banking provider API
        log.debug("Stub: fetching account balance for consent {}", consentId)
        return Money.zero()
    }

    override fun isConsentActive(consentId: String): Boolean {
        // TODO: validate consent with real open banking provider API
        log.debug("Stub: checking consent status for consent {}", consentId)
        return true
    }
}
