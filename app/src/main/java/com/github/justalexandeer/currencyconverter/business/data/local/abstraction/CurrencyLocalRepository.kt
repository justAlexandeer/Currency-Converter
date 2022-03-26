package com.github.justalexandeer.currencyconverter.business.data.local.abstraction

import com.github.justalexandeer.currencyconverter.business.domain.model.Currency

interface CurrencyLocalRepository {
    suspend fun getAllCurrencies(): List<Currency>
    suspend fun saveListOfCurrency(listOfCurrency: List<Currency>)
    suspend fun deleteAllCurrencies()
}