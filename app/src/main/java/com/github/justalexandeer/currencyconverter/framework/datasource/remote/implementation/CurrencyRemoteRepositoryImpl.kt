package com.github.justalexandeer.currencyconverter.framework.datasource.remote.implementation

import com.github.justalexandeer.currencyconverter.business.data.remote.abstraction.CurrencyRemoteRepository
import com.github.justalexandeer.currencyconverter.business.domain.model.Currency
import com.github.justalexandeer.currencyconverter.framework.datasource.remote.ApiCurrency
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CurrencyRemoteRepositoryImpl @Inject constructor(
    private val apiCurrency: ApiCurrency
) : CurrencyRemoteRepository {

    override suspend fun getAllCurrency(): List<Currency> =
        apiCurrency.getAllCurrencies().currencies.entries.map { it.value }

}