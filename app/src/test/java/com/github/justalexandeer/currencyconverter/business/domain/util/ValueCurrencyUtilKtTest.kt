package com.github.justalexandeer.currencyconverter.business.domain.util

import org.hamcrest.MatcherAssert
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsEqual
import org.junit.Test


class ValueCurrencyUtilKtTest {

    private val length = 6

    @Test
    fun convertValueCurrency_valueLengthLessThenMaxLength_stringFullLength() {
        val value = 10.12
        val expectedResult = "10.12"
        val result = convertValueCurrency(value, length)
        assertThat(result, IsEqual(expectedResult))
    }

    @Test
    fun convertValueCurrency_valueLengthMoreThenMaxLength_stringSubstring() {
        val value = 10.12345
        val expectedResult = "10.123"
        val result = convertValueCurrency(value, length)
        assertThat(result, IsEqual(expectedResult))
    }

    @Test
    fun convertValueCurrency_valueLengthMoreThenMaxLengthAndLastCharIsDot_stringSubstring() {
        val value = 12345.678
        val expectedResult = "12345"
        val result = convertValueCurrency(value, length)
        assertThat(result, IsEqual(expectedResult))
    }

    @Test
    fun convertValueCurrency_valueIsZero_stringSubstring() {
        val value = 0.0
        val expectedResult = "0.0"
        val result = convertValueCurrency(value, length)
        assertThat(result, IsEqual(expectedResult))
    }
}