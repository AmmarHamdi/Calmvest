package com.calmvest.domain.model

import java.math.BigDecimal
import java.math.RoundingMode

@JvmInline
value class Money(val minorUnits: Long) {

    companion object {
        fun ofEuros(euros: BigDecimal): Money {
            val units = euros.setScale(2, RoundingMode.HALF_UP)
                .multiply(BigDecimal(100))
                .toLong()
            return Money(units)
        }

        fun zero(): Money = Money(0L)
    }

    fun toEuros(): BigDecimal =
        BigDecimal(minorUnits).divide(BigDecimal(100), 2, RoundingMode.HALF_UP)

    operator fun plus(other: Money): Money = Money(minorUnits + other.minorUnits)

    operator fun minus(other: Money): Money = Money(minorUnits - other.minorUnits)

    operator fun compareTo(other: Money): Int = minorUnits.compareTo(other.minorUnits)

    fun roundUpToNextEuro(): Money {
        if (minorUnits <= 0) return zero()
        val remainder = minorUnits % 100
        if (remainder == 0L) return zero()
        return Money(100L - remainder)
    }

    fun isZero(): Boolean = minorUnits == 0L

    fun isPositive(): Boolean = minorUnits > 0L

    override fun toString(): String = "EUR ${toEuros()}"
}
