package com.github.justalexandeer.currencyconverter.framework.presentation.listcurrency

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.justalexandeer.currencyconverter.business.domain.model.Currency
import com.github.justalexandeer.currencyconverter.business.domain.model.CurrencyViewHolderState
import com.github.justalexandeer.currencyconverter.business.domain.state.DataState
import com.github.justalexandeer.currencyconverter.business.domain.state.DataStateStatus
import com.github.justalexandeer.currencyconverter.business.interactors.GetAllCurrenciesUseCase
import com.github.justalexandeer.currencyconverter.framework.presentation.listcurrency.model.CurrencyListScreenEvent
import com.github.justalexandeer.currencyconverter.framework.presentation.listcurrency.model.CurrencyListScreenState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.log

class CurrencyConverterViewModel @Inject constructor(
    private val getAllCurrenciesUseCase: GetAllCurrenciesUseCase,
) : ViewModel() {

    private val _currencyListScreenState: MutableStateFlow<CurrencyListScreenState> =
        MutableStateFlow(
            CurrencyListScreenState(
                false,
                "",
                null,
                null,
                null
            )
        )
    val currencyListScreenState: StateFlow<CurrencyListScreenState> = _currencyListScreenState

    private var currentConverterValue: String? = null

    fun obtainEvent(event: CurrencyListScreenEvent) {
        when (event) {
            is CurrencyListScreenEvent.OnCurrencyClick -> handleOnCurrencyClick(event.currency)
            is CurrencyListScreenEvent.OnConvertButtonClick -> {
                currentConverterValue = event.value
                handleConverterResult()
            }
        }
    }

    fun getCurrency() {
        viewModelScope.launch {
            getAllCurrenciesUseCase().collect {
                if (it.status == DataStateStatus.Success) {
                    handleSuccessAllCurrenciesUseCase(it)
                } else {
                    handleErrorAllCurrenciesUseCase(it)
                }
            }
        }
    }

    private fun handleOnCurrencyClick(currency: Currency) {
        if(currency != _currencyListScreenState.value.chosenCurrency) {
            _currencyListScreenState.value = _currencyListScreenState.value.copy(
                data = _currencyListScreenState.value.data?.map {
                    if (currency.id == it.currency.id) {
                        it.copy(isBorderVisible = true)
                    } else {
                        it.copy(isBorderVisible = false)
                    }
                },
                convertResult = "",
                chosenCurrency = currency
            )
        }
    }

    private fun handleConverterResult() {
        val chosenCurrency = _currencyListScreenState.value.chosenCurrency
        if (chosenCurrency != null) {
            _currencyListScreenState.value = _currencyListScreenState.value.copy(
                convertResult = if (!currentConverterValue.isNullOrBlank()) {
                    currentConverterValue?.let {
                        (it.toDouble() / chosenCurrency.value).toString()
                    }
                } else {
                    //todo send error
                    null
                }
            )
        } else {
            // todo send error
        }
    }

    private fun handleSuccessAllCurrenciesUseCase(dataState: DataState<List<Currency>>) {
        dataState.data?.let { listOfCurrency ->
            dataState.isDataFromCache?.let {
                val chosenCurrency = _currencyListScreenState.value.chosenCurrency
                if (dataState.isDataFromCache) {
                    _currencyListScreenState.value = _currencyListScreenState.value.copy(
                        data = listOfCurrency.map {
                            if (chosenCurrency?.id == it.id) {
                                CurrencyViewHolderState(it, true)
                            } else {
                                CurrencyViewHolderState(it)
                            }
                        },
                        isLoading = false
                    )
                } else {
                    _currencyListScreenState.value = _currencyListScreenState.value.copy(
                        data = listOfCurrency.map {
                            if (chosenCurrency?.id == it.id) {
                                CurrencyViewHolderState(it, true)
                            } else {
                                CurrencyViewHolderState(it)
                            }
                        },
                        lastUpdate = System.currentTimeMillis(),
                        isLoading = false
                    )
                }
            }
        }
    }

//    private fun changeChosenCurrency(currency: Currency) {
//        _currencyListScreenState.value = _currencyListScreenState.value.copy(
//            chosenCurrency = currency
//        )
//        Log.i("handleConverterResult", "change ${_currencyListScreenState.value.chosenCurrency}")
//    }

    private fun handleErrorAllCurrenciesUseCase(dataState: DataState<List<Currency>>) {
        dataState.errorMessage?.let { errorMessage ->
            //_currencyListScreenState.value = CurrencyListScreenState.Error(errorMessage)
        }
    }

}