package com.github.justalexandeer.currencyconverter.framework.presentation.listcurrency

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

class CurrencyConverterViewModel @Inject constructor(
    private val getAllCurrenciesUseCase: GetAllCurrenciesUseCase,
) : ViewModel() {

    // todo Если пользователь выбрал валюту для конвертации, но после обновления списка она пропала

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

    init {
        viewModelScope.launch {
            getAllCurrenciesUseCase(false, false).collect {
                if (it.status == DataStateStatus.Success) {
                    handleSuccessAllCurrenciesUseCase(it)
                } else {
                    handleErrorAllCurrenciesUseCase(it)
                }
            }
        }
    }

    fun obtainEvent(event: CurrencyListScreenEvent) {
        when (event) {
            is CurrencyListScreenEvent.OnCurrencyClick -> handleChangeChosenCurrency(event.currency)
            is CurrencyListScreenEvent.ConvertValueEditTextChange -> {
                currentConverterValue = event.value
                handleConverterResult()
            }
        }
    }

    fun getCurrency() {
        viewModelScope.launch {
            getAllCurrenciesUseCase(false, true).collect {
                if (it.status == DataStateStatus.Success) {
                    handleSuccessAllCurrenciesUseCase(it)
                } else {
                    handleErrorAllCurrenciesUseCase(it)
                }
            }
        }
    }

    private fun handleChangeChosenCurrency(currency: Currency) {
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
        handleConverterResult()
    }

    private fun handleConverterResult() {
        val chosenCurrency = _currencyListScreenState.value.chosenCurrency
        if (chosenCurrency != null) {
            _currencyListScreenState.value = _currencyListScreenState.value.copy(
                convertResult = if (!currentConverterValue.isNullOrBlank()) {
                    currentConverterValue?.let {
                        (it.toDouble() / (chosenCurrency.value / chosenCurrency.nominal)).toString()
                    }
                } else {
                    //todo send error
                    ""
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
                    updateChosenCurrencyAndConvertResult()
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
                    updateChosenCurrencyAndConvertResult()
                }
            }
        }
    }

    private fun updateChosenCurrencyAndConvertResult() {
        val updatedChosenCurrency = _currencyListScreenState.value.data?.find {
            it.currency.id == _currencyListScreenState.value.chosenCurrency?.id
        }
        updatedChosenCurrency?.let {
            _currencyListScreenState.value = _currencyListScreenState.value.copy(
                chosenCurrency = updatedChosenCurrency.currency
            )
            handleConverterResult()
        }
    }

    private fun handleErrorAllCurrenciesUseCase(dataState: DataState<List<Currency>>) {
        dataState.errorMessage?.let { errorMessage ->
            //_currencyListScreenState.value = CurrencyListScreenState.Error(errorMessage)
        }
    }

}