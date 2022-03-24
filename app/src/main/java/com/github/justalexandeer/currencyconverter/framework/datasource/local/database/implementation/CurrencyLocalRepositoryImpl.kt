package com.github.justalexandeer.currencyconverter.framework.datasource.local.database.implementation

import com.github.justalexandeer.currencyconverter.business.data.local.abstraction.CurrencyLocalRepository
import com.github.justalexandeer.currencyconverter.business.domain.model.Currency
import com.github.justalexandeer.currencyconverter.framework.datasource.local.database.dao.CurrencyEntityDao
import com.github.justalexandeer.currencyconverter.framework.datasource.local.database.mapper.CurrencyEntityMapper
import kotlinx.coroutines.currentCoroutineContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CurrencyLocalRepositoryImpl @Inject constructor(
    private val currencyEntityDao: CurrencyEntityDao,
    private val mapper: CurrencyEntityMapper
): CurrencyLocalRepository {

    override suspend fun getAllCurrencies(): List<Currency> =
        mapper.fromListCurrencyEntityToListCurrency(currencyEntityDao.getAllCurrenciesEntity())

    override suspend fun saveListOfCurrency(listOfCurrency: List<Currency>) =
        currencyEntityDao.saveListOfCurrencyEntity(mapper.fromListCurrencyToListCurrencyEntity(listOfCurrency))
}