package com.github.justalexandeer.currencyconverter.framework.datasource.remote.implementation.fake

import com.github.justalexandeer.currencyconverter.business.data.remote.abstraction.CurrencyRemoteRepository
import com.github.justalexandeer.currencyconverter.business.domain.model.Currency
import org.junit.Assert.*
import java.io.IOException

class FakeCurrencyRemoteRepositoryImpl: CurrencyRemoteRepository {

    val dataFromRemote = mutableListOf<Currency>()
    var exception: Exception? = null

    override suspend fun getAllCurrency(): List<Currency> {
        exception?.let {
            throw exception as Exception
        }
        return dataFromRemote
    }

}