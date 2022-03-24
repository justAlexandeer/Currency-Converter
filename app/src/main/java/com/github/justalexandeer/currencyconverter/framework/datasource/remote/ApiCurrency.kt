package com.github.justalexandeer.currencyconverter.framework.datasource.remote

import com.github.justalexandeer.currencyconverter.framework.datasource.remote.response.MainResponse
import retrofit2.http.GET

interface ApiCurrency {
    @GET("daily_json.js")
    suspend fun getAllCurrencies(): MainResponse
}