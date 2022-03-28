package com.github.justalexandeer.currencyconverter.framework.presentation.listcurrency

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.justalexandeer.currencyconverter.business.domain.model.Currency
import com.github.justalexandeer.currencyconverter.business.domain.model.CurrencyViewHolderState
import com.github.justalexandeer.currencyconverter.business.domain.state.DataState
import com.github.justalexandeer.currencyconverter.business.domain.state.DataStateStatus
import com.github.justalexandeer.currencyconverter.business.domain.state.SortType
import com.github.justalexandeer.currencyconverter.business.domain.util.sortListCurrencyUtil
import com.github.justalexandeer.currencyconverter.business.domain.util.sortListCurrencyViewHolderStateUtil
import com.github.justalexandeer.currencyconverter.business.interactors.GetAllCurrenciesUseCase
import com.github.justalexandeer.currencyconverter.business.interactors.GetLastUpdateCurrenciesUseCase
import com.github.justalexandeer.currencyconverter.business.interactors.SaveLastUpdateCurrenciesUseCase
import com.github.justalexandeer.currencyconverter.di.IoDispatcher
import com.github.justalexandeer.currencyconverter.framework.presentation.listcurrency.model.CurrencyListScreenEvent
import com.github.justalexandeer.currencyconverter.framework.presentation.listcurrency.model.CurrencyListScreenState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class CurrencyConverterViewModel @Inject constructor(
    private val getAllCurrenciesUseCase: GetAllCurrenciesUseCase,
    private val getLastUpdateCurrenciesUseCase: GetLastUpdateCurrenciesUseCase,
    private val saveLastUpdateCurrenciesUseCase: SaveLastUpdateCurrenciesUseCase,
    @IoDispatcher private val dispatcherIO: CoroutineDispatcher
) : ViewModel() {

    private val _currencyListScreenState: MutableStateFlow<CurrencyListScreenState> =
        MutableStateFlow(
            CurrencyListScreenState(
                false,
                null,
                null,
                getLastUpdateCurrenciesUseCase(),
                null
            )
        )
    val currencyListScreenState: StateFlow<CurrencyListScreenState> = _currencyListScreenState
    private val _errors : MutableSharedFlow<String> = MutableSharedFlow()
    val error = _errors.asSharedFlow()

    private var currentSortType: SortType = SortType.AlphabeticalAsc
    private var currentConverterValue: String? = null

    init {
        getListCurrencies(false)
        startAutoUpdateCurrencyList()
    }

    fun obtainEvent(event: CurrencyListScreenEvent) {
        when (event) {
            is CurrencyListScreenEvent.OnCurrencyClick -> {
                handleChangeChosenCurrency(event.currency)
            }
            is CurrencyListScreenEvent.ConvertValueEditTextChange -> {
                currentConverterValue = event.value
                handleConverterResult()
            }
            is CurrencyListScreenEvent.SortTypeChange -> {
                handleSortTypeChange(event.sortType)
            }
            is CurrencyListScreenEvent.ForceUpdate -> {
                getListCurrencies(true)
            }
        }
    }

    private fun getListCurrencies(forceUpdate: Boolean) {
        viewModelScope.launch {
            _currencyListScreenState.value = _currencyListScreenState.value.copy(
                isLoading = true
            )
            getAllCurrenciesUseCase(forceUpdate).collect {
                if (it.status == DataStateStatus.Success) {
                    handleSuccessAllCurrenciesUseCase(it)
                } else {
                    handleErrorAllCurrenciesUseCase(it)
                }
            }
        }
    }

    private fun handleChangeChosenCurrency(currency: Currency) {
        if (currency != _currencyListScreenState.value.chosenCurrency) {
            _currencyListScreenState.value = _currencyListScreenState.value.copy(
                data = _currencyListScreenState.value.data?.map {
                    if (currency.id == it.currency.id) {
                        it.copy(isBorderVisible = true)
                    } else {
                        it.copy(isBorderVisible = false)
                    }
                },
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
                    null
                }
            )
        } else {
            _currencyListScreenState.value = _currencyListScreenState.value.copy(
                convertResult = null
            )
        }
    }

    private fun handleSuccessAllCurrenciesUseCase(dataState: DataState<List<Currency>>) {
        dataState.data?.let { listOfCurrency ->
            dataState.isDataFromCache?.let {
                val chosenCurrency = _currencyListScreenState.value.chosenCurrency
                val sortedListOfCurrency = sortListCurrencyUtil(currentSortType, listOfCurrency)
                if (dataState.isDataFromCache) {
                    _currencyListScreenState.value = _currencyListScreenState.value.copy(
                        data = sortedListOfCurrency.map {
                            if (chosenCurrency?.id == it.id) {
                                CurrencyViewHolderState(it, true)
                            } else {
                                CurrencyViewHolderState(it)
                            }
                        },
                        isLoading = false
                    )
                } else {
                    saveLastUpdateCurrenciesUseCase(System.currentTimeMillis())
                    _currencyListScreenState.value = _currencyListScreenState.value.copy(
                        data = sortedListOfCurrency.map {
                            if (chosenCurrency?.id == it.id) {
                                CurrencyViewHolderState(it, true)
                            } else {
                                CurrencyViewHolderState(it)
                            }
                        },
                        lastUpdate = getLastUpdateCurrenciesUseCase(),
                        isLoading = false
                    )
                }
                updateChosenCurrencyAndConvertResult()
            }
        }
    }

    private fun handleErrorAllCurrenciesUseCase(dataState: DataState<List<Currency>>) {
        dataState.errorMessage?.let { errorMessage ->
            viewModelScope.launch {
                _errors.emit(errorMessage)
            }
        }
        _currencyListScreenState.value = _currencyListScreenState.value.copy(
            isLoading = false
        )
    }

    private fun updateChosenCurrencyAndConvertResult() {
        val updatedChosenCurrency = _currencyListScreenState.value.data?.find {
            it.currency.id == _currencyListScreenState.value.chosenCurrency?.id
        }
        _currencyListScreenState.value = _currencyListScreenState.value.copy(
            chosenCurrency = updatedChosenCurrency?.currency
        )
        handleConverterResult()
    }

    private fun handleSortTypeChange(sortType: SortType) {
        currentSortType = sortType
        _currencyListScreenState.value = _currencyListScreenState.value.copy(
            data = _currencyListScreenState.value.data?.let {
                sortListCurrencyViewHolderStateUtil(currentSortType,
                    it
                )
            }
        )
    }

    private fun startAutoUpdateCurrencyList() {
        viewModelScope.launch(dispatcherIO) {
            while (true) {
                delay(AUTO_UPDATE_DURATION)
                getAllCurrenciesUseCase(true).collect {
                    if (it.status == DataStateStatus.Success) {
                        handleSuccessAllCurrenciesUseCase(it)
                    } else {
                        handleErrorAllCurrenciesUseCase(it)
                    }
                }
            }
        }
    }

    companion object {
        private const val AUTO_UPDATE_DURATION = 60 * 1000L
    }

}