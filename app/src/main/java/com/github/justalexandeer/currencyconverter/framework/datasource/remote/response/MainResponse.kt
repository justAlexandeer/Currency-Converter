package com.github.justalexandeer.currencyconverter.framework.datasource.remote.response

import com.github.justalexandeer.currencyconverter.business.domain.model.Currency
import com.google.gson.annotations.SerializedName

data class MainResponse (
    @SerializedName("Date")
    val date: String,
    @SerializedName("PreviousDate")
    val previousDate: String,
    @SerializedName("PreviousURL")
    val previousURL: String,
    @SerializedName("Timestamp")
    val timestamp: String,
    @SerializedName("Valute")
    val currencies: Map<String, Currency>
)