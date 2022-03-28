package com.github.justalexandeer.currencyconverter.business.domain.util

import com.github.justalexandeer.currencyconverter.business.domain.model.Currency
import com.github.justalexandeer.currencyconverter.business.domain.model.CurrencyViewHolderState
import com.github.justalexandeer.currencyconverter.business.domain.state.SortType

fun sortListCurrencyUtil(
    sortType: SortType,
    unsortedList: List<Currency>
): List<Currency> {
    return when (sortType) {
        SortType.AlphabeticalAsc -> unsortedList.sortedBy { it.charCode }
        SortType.AlphabeticalDesc -> unsortedList.sortedByDescending { it.charCode }
        SortType.ValueAsc -> unsortedList.sortedBy { it.nominal / it.value }
        SortType.ValueDesc -> unsortedList.sortedByDescending { it.nominal / it.value }
    }
}

fun sortListCurrencyViewHolderStateUtil(
    sortType: SortType,
    unsortedList: List<CurrencyViewHolderState>
): List<CurrencyViewHolderState> {
    return when (sortType) {
        SortType.AlphabeticalAsc -> unsortedList.sortedBy { it.currency.charCode }
        SortType.AlphabeticalDesc -> unsortedList.sortedByDescending { it.currency.charCode }
        SortType.ValueAsc -> unsortedList.sortedBy { it.currency.nominal / it.currency.value }
        SortType.ValueDesc -> unsortedList.sortedByDescending { it.currency.nominal / it.currency.value }
    }
}