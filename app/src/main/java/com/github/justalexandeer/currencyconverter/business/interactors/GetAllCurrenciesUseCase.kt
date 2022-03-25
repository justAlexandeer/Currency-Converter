package com.github.justalexandeer.currencyconverter.business.interactors

import com.github.justalexandeer.currencyconverter.business.data.local.abstraction.CurrencyLocalRepository
import com.github.justalexandeer.currencyconverter.business.data.remote.abstraction.CurrencyRemoteRepository
import com.github.justalexandeer.currencyconverter.business.data.remote.safeApiCall
import com.github.justalexandeer.currencyconverter.business.domain.model.Currency
import com.github.justalexandeer.currencyconverter.business.domain.state.DataState
import com.github.justalexandeer.currencyconverter.business.domain.state.DataStateStatus
import com.github.justalexandeer.currencyconverter.framework.presentation.listcurrency.model.CurrencyListScreenState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetAllCurrenciesUseCase @Inject constructor(
    private val currencyLocalRepository: CurrencyLocalRepository,
    private val currencyRemoteRepository: CurrencyRemoteRepository,
    private val dispatcherIO: CoroutineDispatcher
) {

    suspend operator fun invoke(): Flow<DataState<List<Currency>>> = flow {
        var localDataState = currencyLocalRepository.getAllCurrencies()
        emit(createDataStateSuccess(localDataState, true))

        val remoteDataState = safeApiCall(dispatcherIO) {
            currencyRemoteRepository.getAllCurrency()
        }
        if (remoteDataState.status == DataStateStatus.Success) {
            if (!remoteDataState.data.isNullOrEmpty()) {
                currencyLocalRepository.saveListOfCurrency(remoteDataState.data)
                localDataState = currencyLocalRepository.getAllCurrencies()
                emit(createDataStateSuccess(localDataState, false))
            } else {
                emit(createDataStateError(REMOTE_DATA_IS_NULL_OR_EMPTY))
            }
        } else {
            remoteDataState.errorMessage?.let {
                emit(createDataStateError(remoteDataState.errorMessage))
            }
        }
    }

    private fun createDataStateSuccess(
        listOfCurrency: List<Currency>,
        isDataFromCache: Boolean
    ): DataState<List<Currency>> {
        return DataState(DataStateStatus.Success, listOfCurrency, isDataFromCache, null, null)
    }

    private fun createDataStateError(errorMessage: String): DataState<List<Currency>> {
        return DataState(DataStateStatus.Error, null, null, errorMessage, null)
    }

}

private const val REMOTE_DATA_IS_NULL_OR_EMPTY = "Data from internet is empty"