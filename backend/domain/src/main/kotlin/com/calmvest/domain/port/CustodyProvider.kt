package com.calmvest.domain.port

import com.calmvest.domain.model.InvestmentMode
import com.calmvest.domain.model.Money

/**
 * Port for interacting with the custody/brokerage provider to execute investment orders.
 */
interface CustodyProvider {
    /**
     * Submits an investment order to the custody provider.
     * Returns the provider-assigned order ID.
     */
    fun submitOrder(
        idempotencyKey: String,
        amount: Money,
        mode: InvestmentMode,
        userReference: String
    ): String

    /**
     * Retrieves the current market value of the portfolio for a given user reference.
     */
    fun getPortfolioValue(userReference: String, mode: InvestmentMode): Money
}
