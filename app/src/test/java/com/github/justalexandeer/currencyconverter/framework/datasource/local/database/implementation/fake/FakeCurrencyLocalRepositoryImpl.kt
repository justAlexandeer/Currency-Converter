package com.github.justalexandeer.currencyconverter.framework.datasource.local.database.implementation.fake

import com.github.justalexandeer.currencyconverter.business.data.local.abstraction.CurrencyLocalRepository
import com.github.justalexandeer.currencyconverter.business.domain.model.Currency
import org.junit.Assert.*

import org.junit.Test

class FakeCurrencyLocalRepositoryImpl : CurrencyLocalRepository {

    val dataInLocal = mutableListOf<Currency>()

    override suspend fun getAllCurrencies(): List<Currency> {
        return dataInLocal
    }

    override suspend fun saveListOfCurrency(listOfCurrency: List<Currency>) {
        dataInLocal.addAll(listOfCurrency)
    }

    override suspend fun deleteAllCurrencies() {
        dataInLocal.clear()
    }

}