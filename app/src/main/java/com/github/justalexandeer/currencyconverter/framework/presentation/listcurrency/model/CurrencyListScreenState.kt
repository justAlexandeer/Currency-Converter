package com.github.justalexandeer.currencyconverter.framework.presentation.listcurrency.model

import com.github.justalexandeer.currencyconverter.business.domain.model.Currency
import com.github.justalexandeer.currencyconverter.business.domain.model.CurrencyViewHolderState

data class CurrencyListScreenState (
    val isLoading: Boolean = false,
    val convertResult: String?,
    val chosenCurrency: Currency?,
    val lastUpdate: Long?,
    val data: List<CurrencyViewHolderState>?
)