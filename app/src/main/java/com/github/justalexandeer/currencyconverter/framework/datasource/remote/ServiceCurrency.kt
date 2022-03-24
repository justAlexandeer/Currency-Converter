package com.github.justalexandeer.currencyconverter.framework.datasource.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ServiceCurrency @Inject constructor() {
    val retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        .build()
}

private const val BASE_URL = "https://www.cbr-xml-daily.ru"