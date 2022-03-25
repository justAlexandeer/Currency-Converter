package com.github.justalexandeer.currencyconverter.framework.presentation.listcurrency.model

import com.github.justalexandeer.currencyconverter.business.domain.model.Currency

sealed class CurrencyListScreenEvent {
    data class OnCurrencyClick(val currency: Currency): CurrencyListScreenEvent()
    data class OnConvertButtonClick(val value: String): CurrencyListScreenEvent()
}