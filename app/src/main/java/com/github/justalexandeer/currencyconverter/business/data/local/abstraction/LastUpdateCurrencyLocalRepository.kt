package com.github.justalexandeer.currencyconverter.business.data.local.abstraction

interface LastUpdateCurrencyLocalRepository {
    fun saveLastUpdateDate(date: Long)
    fun getLastUpdateDate(): Long?
}