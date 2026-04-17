package com.calmvest.domain.model

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.math.BigDecimal

class MoneyTest {

    @Test
    fun `ofEuros converts correctly to minor units`() {
        assertEquals(123L, Money.ofEuros(BigDecimal("1.23")).minorUnits)
        assertEquals(100L, Money.ofEuros(BigDecimal("1.00")).minorUnits)
        assertEquals(999L, Money.ofEuros(BigDecimal("9.99")).minorUnits)
        assertEquals(100000L, Money.ofEuros(BigDecimal("1000.00")).minorUnits)
    }

    @Test
    fun `ofEuros rounds half up to 2dp`() {
        assertEquals(124L, Money.ofEuros(BigDecimal("1.235")).minorUnits)
        assertEquals(123L, Money.ofEuros(BigDecimal("1.234")).minorUnits)
    }

    @Test
    fun `toEuros converts minor units back to BigDecimal`() {
        assertEquals(BigDecimal("1.23"), Money(123L).toEuros())
        assertEquals(BigDecimal("0.00"), Money(0L).toEuros())
        assertEquals(BigDecimal("10.00"), Money(1000L).toEuros())
    }

    @Test
    fun `plus adds two Money values`() {
        val a = Money(100L)
        val b = Money(50L)
        assertEquals(Money(150L), a + b)
    }

    @Test
    fun `minus subtracts two Money values`() {
        val a = Money(200L)
        val b = Money(75L)
        assertEquals(Money(125L), a - b)
    }

    @Test
    fun `compareTo compares by minor units`() {
        assertTrue(Money(200L) > Money(100L))
        assertTrue(Money(100L) < Money(200L))
        assertEquals(0, Money(100L).compareTo(Money(100L)))
    }

    @Test
    fun `roundUpToNextEuro returns zero for exact euro`() {
        assertEquals(Money.zero(), Money.ofEuros(BigDecimal("2.00")).roundUpToNextEuro())
        assertEquals(Money.zero(), Money.ofEuros(BigDecimal("10.00")).roundUpToNextEuro())
    }

    @Test
    fun `roundUpToNextEuro returns correct cents`() {
        // 1.30 -> round up to 2.00 -> 70 cents
        assertEquals(Money(70L), Money.ofEuros(BigDecimal("1.30")).roundUpToNextEuro())
        // 4.01 -> round up to 5.00 -> 99 cents
        assertEquals(Money(99L), Money.ofEuros(BigDecimal("4.01")).roundUpToNextEuro())
        // 9.99 -> round up to 10.00 -> 1 cent
        assertEquals(Money(1L), Money.ofEuros(BigDecimal("9.99")).roundUpToNextEuro())
    }

    @Test
    fun `roundUpToNextEuro returns zero for zero amount`() {
        assertEquals(Money.zero(), Money.zero().roundUpToNextEuro())
    }

    @Test
    fun `isZero returns true only for zero`() {
        assertTrue(Money.zero().isZero())
        assertFalse(Money(1L).isZero())
        assertFalse(Money(-1L).isZero())
    }

    @Test
    fun `isPositive returns true only for positive amounts`() {
        assertTrue(Money(1L).isPositive())
        assertFalse(Money(0L).isPositive())
        assertFalse(Money(-1L).isPositive())
    }

    @Test
    fun `zero factory returns zero money`() {
        assertEquals(0L, Money.zero().minorUnits)
    }

    @Test
    fun `toString formats as EUR`() {
        assertEquals("EUR 1.23", Money(123L).toString())
    }
}
