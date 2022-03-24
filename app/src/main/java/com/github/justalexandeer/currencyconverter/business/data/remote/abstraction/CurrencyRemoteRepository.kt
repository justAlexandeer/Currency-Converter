package com.github.justalexandeer.currencyconverter.business.data.remote.abstraction

import com.github.justalexandeer.currencyconverter.business.domain.model.Currency

interface CurrencyRemoteRepository {
    suspend fun getAllCurrency(): List<Currency>
}