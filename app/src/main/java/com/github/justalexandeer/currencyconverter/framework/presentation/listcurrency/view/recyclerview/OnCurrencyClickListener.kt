package com.github.justalexandeer.currencyconverter.framework.presentation.listcurrency.view.recyclerview

import com.github.justalexandeer.currencyconverter.business.domain.model.Currency

interface OnCurrencyClickListener {
    fun onCurrencyClick(currency: Currency)
}