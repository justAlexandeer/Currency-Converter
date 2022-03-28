package com.github.justalexandeer.currencyconverter.business.domain.util

import com.github.justalexandeer.currencyconverter.business.domain.model.Currency
import org.junit.Assert.*
import java.util.*

val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')

fun createListOfRandomCurrencies(): List<Currency> {
    val countCurrency = (1..20).random()
    val listCurrency = mutableListOf<Currency>()
    for (i in 0..countCurrency) {
        listCurrency.add(
            Currency(
                (1..3).map { allowedChars.random() }.joinToString(""),
                (1..5).map { allowedChars.random() }.joinToString(""),
                (1..10).map { allowedChars.random() }.joinToString(""),
                1,
                (1..5).map { allowedChars.random() }.joinToString(""),
                (1..100).random().toDouble(),
                (1..100).random().toDouble()
            )
        )
    }
    return listCurrency
}