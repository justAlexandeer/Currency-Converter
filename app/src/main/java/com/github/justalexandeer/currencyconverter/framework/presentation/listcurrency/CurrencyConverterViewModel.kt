package com.github.justalexandeer.currencyconverter.framework.presentation.listcurrency

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.justalexandeer.currencyconverter.business.domain.model.Currency
import com.github.justalexandeer.currencyconverter.business.domain.state.DataState
import com.github.justalexandeer.currencyconverter.business.domain.state.DataStateStatus
import com.github.justalexandeer.currencyconverter.business.interactors.GetAllCurrenciesUseCase
import com.github.justalexandeer.currencyconverter.di.IoDispatcher
import com.github.justalexandeer.currencyconverter.framework.presentation.listcurrency.model.CurrencyListScreenState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class CurrencyConverterViewModel @Inject constructor(
    private val getAllCurrenciesUseCase: GetAllCurrenciesUseCase,
) : ViewModel() {

    private val _currencyListScreenState: MutableStateFlow<CurrencyListScreenState> =
        MutableStateFlow(CurrencyListScreenState(false, null,null))
    val currencyListScreenState: StateFlow<CurrencyListScreenState> = _currencyListScreenState

    fun getCurrency() {
        viewModelScope.launch {
            getAllCurrenciesUseCase().collect {
                if(it.status == DataStateStatus.Success) {
                    handleSuccessAllCurrenciesUseCase(it)
                } else {
                    handleErrorAllCurrenciesUseCase(it)
                }
            }
        }
    }

    private fun handleSuccessAllCurrenciesUseCase(dataState: DataState<List<Currency>>) {
        dataState.data?.let { listOfCurrency ->
            dataState.isDataFromCache?.let {
                if(dataState.isDataFromCache) {
                    _currencyListScreenState.value = _currencyListScreenState.value.copy(
                        data = listOfCurrency,
                        isLoading = false
                    )
                } else {
                    _currencyListScreenState.value = _currencyListScreenState.value.copy(
                        data = listOfCurrency,
                        lastUpdate = System.currentTimeMillis(),
                        isLoading = false
                    )
                }
            }
        }
    }

    private fun handleErrorAllCurrenciesUseCase(dataState: DataState<List<Currency>>) {
        dataState.errorMessage?.let { errorMessage ->
            //_currencyListScreenState.value = CurrencyListScreenState.Error(errorMessage)
        }
    }

}