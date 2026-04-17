package com.calmvest.core.domain.model

data class ReserveSummary(
    val userId: String,
    val totalReserveMinorUnits: Long,
    val pendingRoundUpsMinorUnits: Long,
    val thisMonthRoundUpsMinorUnits: Long
) {
    val totalReserve: Money get() = Money.ofMinorUnits(totalReserveMinorUnits)
    val pendingRoundUps: Money get() = Money.ofMinorUnits(pendingRoundUpsMinorUnits)
    val thisMonthRoundUps: Money get() = Money.ofMinorUnits(thisMonthRoundUpsMinorUnits)
}
