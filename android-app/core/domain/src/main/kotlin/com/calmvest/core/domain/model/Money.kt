package com.calmvest.core.domain.model

import java.math.BigDecimal
import java.math.RoundingMode

@JvmInline
value class Money(val minorUnits: Long) {
    fun toEuros(): BigDecimal = BigDecimal(minorUnits).divide(BigDecimal(100), 2, RoundingMode.HALF_UP)
    fun formatEuros(): String = "€%.2f".format(toEuros())
    operator fun plus(other: Money) = Money(minorUnits + other.minorUnits)
    operator fun minus(other: Money) = Money(minorUnits - other.minorUnits)
    operator fun compareTo(other: Money) = minorUnits.compareTo(other.minorUnits)

    companion object {
        fun ofMinorUnits(units: Long) = Money(units)
        fun ofEuros(euros: BigDecimal) = Money((euros * BigDecimal(100)).toLong())
        fun zero() = Money(0L)
    }
}
