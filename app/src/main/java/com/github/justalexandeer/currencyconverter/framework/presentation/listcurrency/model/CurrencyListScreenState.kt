package com.github.justalexandeer.currencyconverter.framework.presentation.listcurrency.model

import com.github.justalexandeer.currencyconverter.business.domain.model.Currency

data class CurrencyListScreenState (
    val isLoading: Boolean = false,
    val lastUpdate: Long?,
    val data: List<Currency>?
)