package com.calmvest.core.domain.model

data class RoundUpRule(
    val id: String,
    val userId: String,
    val isEnabled: Boolean,
    val monthlyCapMinorUnits: Long,
    val thresholdMinorUnits: Long,
    val pauseUntil: String?
) {
    val monthlyCap: Money get() = Money.ofMinorUnits(monthlyCapMinorUnits)
    val threshold: Money get() = Money.ofMinorUnits(thresholdMinorUnits)
}
