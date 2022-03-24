package com.github.justalexandeer.currencyconverter.framework.presentation.listcurrency

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.justalexandeer.currencyconverter.business.data.local.abstraction.CurrencyLocalRepository
import com.github.justalexandeer.currencyconverter.business.data.remote.abstraction.CurrencyRemoteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class CurrencyConverterViewModel @Inject constructor(
    private val currencyLocalRepository: CurrencyLocalRepository,
    private val currencyRemoteRepository: CurrencyRemoteRepository,
) : ViewModel() {

    fun getCurrency() {
        viewModelScope.launch(Dispatchers.IO) {
            val test1 = currencyRemoteRepository.getAllCurrency()
            currencyLocalRepository.saveListOfCurrency(test1)
        }

    }

}