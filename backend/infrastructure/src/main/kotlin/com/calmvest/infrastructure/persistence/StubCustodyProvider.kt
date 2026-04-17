package com.calmvest.infrastructure.persistence

import com.calmvest.domain.model.InvestmentMode
import com.calmvest.domain.model.Money
import com.calmvest.domain.port.CustodyProvider
import org.springframework.stereotype.Component
import java.util.UUID

/**
 * Stub implementation of CustodyProvider.
 * Replace this with a real custody/brokerage API integration.
 */
@Component
class StubCustodyProvider : CustodyProvider {

    override fun submitOrder(
        idempotencyKey: String,
        amount: Money,
        mode: InvestmentMode,
        userReference: String
    ): String {
        // TODO: integrate with real custody provider API
        return "stub-provider-order-${UUID.randomUUID()}"
    }

    override fun getPortfolioValue(userReference: String, mode: InvestmentMode): Money {
        // TODO: fetch real portfolio value from custody provider API
        return Money.zero()
    }
}
