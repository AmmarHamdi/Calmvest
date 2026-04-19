package com.calmvest.core.domain.model

enum class InvestmentMode {
    SAFE, BITCOIN, DIVERSIFIED
}

data class Portfolio(
    val id: String,
    val userId: String,
    val mode: InvestmentMode,
    val totalInvestedMinorUnits: Long,
    val currentValueMinorUnits: Long
) {
    val totalInvested: Money get() = Money.ofMinorUnits(totalInvestedMinorUnits)
    val currentValue: Money get() = Money.ofMinorUnits(currentValueMinorUnits)

    val gainLossMinorUnits: Long get() = currentValueMinorUnits - totalInvestedMinorUnits
    val gainLoss: Money get() = Money.ofMinorUnits(gainLossMinorUnits)

    val gainLossPercent: Float
        get() = if (totalInvestedMinorUnits == 0L) 0f
                else (gainLossMinorUnits.toFloat() / totalInvestedMinorUnits.toFloat()) * 100f

    val isGain: Boolean get() = gainLossMinorUnits >= 0

    val modeLabel: String get() = when (mode) {
        InvestmentMode.SAFE -> "Safe"
        InvestmentMode.BITCOIN -> "Bitcoin"
        InvestmentMode.DIVERSIFIED -> "Diversified"
    }

    val modeEmoji: String get() = when (mode) {
        InvestmentMode.SAFE -> "💰"
        InvestmentMode.BITCOIN -> "₿"
        InvestmentMode.DIVERSIFIED -> "🌍"
    }
}
