package com.github.justalexandeer.currencyconverter.framework.datasource.local.preferences.implementation

import com.github.justalexandeer.currencyconverter.business.data.local.abstraction.LastUpdateCurrencyLocalRepository

class FakeLastUpdateCurrencyLocalRepositoryImpl: LastUpdateCurrencyLocalRepository {

    var lastUpdateData: Long? = null

    override fun saveLastUpdateDate(date: Long) {
       lastUpdateData = date
    }

    override fun getLastUpdateDate(): Long? = lastUpdateData

}