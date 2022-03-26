package com.github.justalexandeer.currencyconverter.business.domain.util

fun valueCurrency(value: Double): String {
    val valueString = value.toString()
    return if (valueString.length > 6) {
        val concatValue = valueString.substring(0, 6)
        if (concatValue.last() == '.') {
            concatValue.dropLast(1)
        } else {
            concatValue
        }
    } else {
        valueString
    }
}