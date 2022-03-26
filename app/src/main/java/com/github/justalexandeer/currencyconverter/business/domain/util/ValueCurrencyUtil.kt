package com.github.justalexandeer.currencyconverter.business.domain.util

fun valueCurrency(value: Double, maxLength: Int): String {
    val valueString = value.toString()
    return if (valueString.length > maxLength) {
        val concatValue = valueString.substring(0, maxLength)
        if (concatValue.last() == '.') {
            concatValue.dropLast(1)
        } else {
            concatValue
        }
    } else {
        valueString
    }
}