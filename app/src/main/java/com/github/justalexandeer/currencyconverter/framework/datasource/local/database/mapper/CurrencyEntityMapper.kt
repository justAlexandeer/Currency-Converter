package com.github.justalexandeer.currencyconverter.framework.datasource.local.database.mapper

import com.github.justalexandeer.currencyconverter.business.domain.model.Currency
import com.github.justalexandeer.currencyconverter.framework.datasource.local.database.entity.CurrencyEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CurrencyEntityMapper @Inject constructor() {
    fun fromListCurrencyEntityToListCurrency(listOfCurrencyEntity: List<CurrencyEntity>): List<Currency> =
        listOfCurrencyEntity.map {
            Currency(
                charCode = it.charCode,
                id = it.id,
                name = it.name,
                nominal = it.nominal,
                numCode = it.numCode,
                previous = it.previous,
                value = it.value
            )
        }

    fun fromListCurrencyToListCurrencyEntity(listOfCurrency: List<Currency>): List<CurrencyEntity> =
        listOfCurrency.map {
            CurrencyEntity(
                charCode = it.charCode,
                id = it.id,
                name = it.name,
                nominal = it.nominal,
                numCode = it.numCode,
                previous = it.previous,
                value = it.value
            )
        }
}