package com.github.justalexandeer.currencyconverter.framework.presentation.listcurrency.model

import com.github.justalexandeer.currencyconverter.business.domain.model.Currency
import com.github.justalexandeer.currencyconverter.business.domain.state.SortType

sealed class CurrencyListScreenEvent {
    data class OnCurrencyClick(val currency: Currency): CurrencyListScreenEvent()
    data class ConvertValueEditTextChange(val value: String): CurrencyListScreenEvent()
    data class SortTypeChange(val sortType: SortType): CurrencyListScreenEvent()
    object ForceUpdate: CurrencyListScreenEvent()
}