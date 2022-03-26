package com.github.justalexandeer.currencyconverter.framework.datasource.local.preferences.implementation

import com.github.justalexandeer.currencyconverter.business.data.local.abstraction.LastUpdateCurrencyLocalRepository
import com.github.justalexandeer.currencyconverter.framework.datasource.local.preferences.SharedPreferencesManager
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LastUpdateCurrencyLocalRepositoryImpl @Inject constructor(
    private val sharedPreferencesManager: SharedPreferencesManager
) : LastUpdateCurrencyLocalRepository {

    override fun saveLastUpdateDate(date: Long) = sharedPreferencesManager.saveLastUpdateDate(date)

    override fun getLastUpdateDate(): Long? = sharedPreferencesManager.getLastUpdateDate()

}