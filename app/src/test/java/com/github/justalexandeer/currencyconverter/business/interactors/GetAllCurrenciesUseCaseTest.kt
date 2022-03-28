package com.github.justalexandeer.currencyconverter.business.interactors

import android.util.Log
import com.github.justalexandeer.currencyconverter.business.domain.model.Currency
import com.github.justalexandeer.currencyconverter.business.domain.state.DataState
import com.github.justalexandeer.currencyconverter.business.domain.state.DataStateErrorType
import com.github.justalexandeer.currencyconverter.business.domain.state.DataStateStatus
import com.github.justalexandeer.currencyconverter.business.domain.util.createListOfRandomCurrencies
import com.github.justalexandeer.currencyconverter.framework.datasource.local.database.implementation.fake.FakeCurrencyLocalRepositoryImpl
import com.github.justalexandeer.currencyconverter.framework.datasource.remote.implementation.fake.FakeCurrencyRemoteRepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsEqual
import org.junit.After
import org.junit.Test
import java.io.IOException

@ExperimentalCoroutinesApi
class GetAllCurrenciesUseCaseTest {
    private var fakeCurrencyLocalRepositoryImpl = FakeCurrencyLocalRepositoryImpl()
    private var fakeCurrencyRemoteRepositoryImpl = FakeCurrencyRemoteRepositoryImpl()
    private val getAllCurrenciesUseCase = GetAllCurrenciesUseCase(
        fakeCurrencyLocalRepositoryImpl,
        fakeCurrencyRemoteRepositoryImpl,
        Dispatchers.Unconfined
    )

    @After
    fun resetFakeRepository() {
        fakeCurrencyLocalRepositoryImpl = FakeCurrencyLocalRepositoryImpl()
        fakeCurrencyRemoteRepositoryImpl = FakeCurrencyRemoteRepositoryImpl()
    }

    @Test
    fun useCase_notForceUpdateAndInLocalNoData_firstLoadEmptyListFromLocal() = runTest {
        val expectedList = mutableListOf<Currency>()
        val expectedDataState = DataState(
            DataStateStatus.Success,
            expectedList,
            true,
            null,
            null
        )

        fakeCurrencyLocalRepositoryImpl.dataInLocal.addAll(expectedList)
        val randomList = createListOfRandomCurrencies()
        fakeCurrencyRemoteRepositoryImpl.dataFromRemote.addAll(randomList)

        val dataState = getAllCurrenciesUseCase(false).first()
        assertThat(dataState.status, IsEqual(expectedDataState.status))
        assertThat(dataState.data, IsEqual(expectedDataState.data))
        assertThat(dataState.isDataFromCache, IsEqual(expectedDataState.isDataFromCache))
        assertThat(dataState.errorMessage, IsEqual(expectedDataState.errorMessage))
        assertThat(dataState.errorType, IsEqual(expectedDataState.errorType))
    }

    @Test
    fun useCase_notForceUpdateAndLocalHaveData_firstLoadDataFromLocal() = runTest {
        val expectedList = createListOfRandomCurrencies()
        val expectedDataState = DataState(
            DataStateStatus.Success,
            expectedList,
            true,
            null,
            null
        )

        fakeCurrencyLocalRepositoryImpl.dataInLocal.addAll(expectedList)
        val randomList = createListOfRandomCurrencies()
        fakeCurrencyRemoteRepositoryImpl.dataFromRemote.addAll(randomList)

        val dataState = getAllCurrenciesUseCase(false).first()
        assertThat(dataState.status, IsEqual(expectedDataState.status))
        assertThat(dataState.data, IsEqual(expectedDataState.data))
        assertThat(dataState.isDataFromCache, IsEqual(expectedDataState.isDataFromCache))
        assertThat(dataState.errorMessage, IsEqual(expectedDataState.errorMessage))
        assertThat(dataState.errorType, IsEqual(expectedDataState.errorType))
    }

    @Test
    fun useCase_notForceUpdateAndLocalHaveData_remoteDataUpdateLocalData() = runTest {
        val listInLocal = createListOfRandomCurrencies()
        fakeCurrencyLocalRepositoryImpl.dataInLocal.addAll(listInLocal)
        val listInRemote = createListOfRandomCurrencies()
        fakeCurrencyRemoteRepositoryImpl.dataFromRemote.addAll(listInRemote)

        getAllCurrenciesUseCase(false).collect {  }
        assertThat(fakeCurrencyLocalRepositoryImpl.dataInLocal, IsEqual(listInRemote))
    }

    @Test
    fun useCase_notForceUpdateAndLocalHaveDataAndRemoteError_remoteError() = runTest {
        val expectedDataState = DataState(
            DataStateStatus.Error,
            null,
            null,
            "No internet",
            DataStateErrorType.Remote
        )

        val listInLocal = createListOfRandomCurrencies()
        fakeCurrencyLocalRepositoryImpl.dataInLocal.addAll(listInLocal)
        fakeCurrencyRemoteRepositoryImpl.exception = IOException()

        val dataState = getAllCurrenciesUseCase(false).drop(1).first()
        assertThat(dataState.status, IsEqual(expectedDataState.status))
        assertThat(dataState.data, IsEqual(expectedDataState.data))
        assertThat(dataState.isDataFromCache, IsEqual(expectedDataState.isDataFromCache))
        assertThat(dataState.errorMessage, IsEqual(expectedDataState.errorMessage))
        assertThat(dataState.errorType, IsEqual(expectedDataState.errorType))
    }

    @Test
    fun useCase_notForceUpdateAndInLocalHaveDataAndRemoteSuccess_remoteSuccess() = runTest {
        val expectedList = createListOfRandomCurrencies()
        val expectedDataState = DataState(
            DataStateStatus.Success,
            expectedList,
            false,
            null,
            null
        )

        val randomList = createListOfRandomCurrencies()
        fakeCurrencyLocalRepositoryImpl.dataInLocal.addAll(randomList)
        fakeCurrencyRemoteRepositoryImpl.dataFromRemote.addAll(expectedList)

        val dataState = getAllCurrenciesUseCase(false).drop(1).first()
        assertThat(dataState.status, IsEqual(expectedDataState.status))
        assertThat(dataState.data, IsEqual(expectedDataState.data))
        assertThat(dataState.isDataFromCache, IsEqual(expectedDataState.isDataFromCache))
        assertThat(dataState.errorMessage, IsEqual(expectedDataState.errorMessage))
        assertThat(dataState.errorType, IsEqual(expectedDataState.errorType))
    }

    @Test
    fun useCase_forceUpdate_firstLoadFromRemote() = runTest {
        val expectedList = createListOfRandomCurrencies()
        val expectedDataState = DataState(
            DataStateStatus.Success,
            expectedList,
            false,
            null,
            null
        )

        val randomList = createListOfRandomCurrencies()
        fakeCurrencyLocalRepositoryImpl.dataInLocal.addAll(randomList)
        fakeCurrencyRemoteRepositoryImpl.dataFromRemote.addAll(expectedList)

        val dataState = getAllCurrenciesUseCase(true).first()
        assertThat(dataState.status, IsEqual(expectedDataState.status))
        assertThat(dataState.data, IsEqual(expectedDataState.data))
        assertThat(dataState.isDataFromCache, IsEqual(expectedDataState.isDataFromCache))
        assertThat(dataState.errorMessage, IsEqual(expectedDataState.errorMessage))
        assertThat(dataState.errorType, IsEqual(expectedDataState.errorType))
    }

    @Test
    fun useCase_forceUpdate_remoteDataUpdateLocalData() = runTest {
        val expectedList = createListOfRandomCurrencies()
        val randomList = createListOfRandomCurrencies()
        fakeCurrencyLocalRepositoryImpl.dataInLocal.addAll(randomList)
        fakeCurrencyRemoteRepositoryImpl.dataFromRemote.addAll(expectedList)

        getAllCurrenciesUseCase(true).collect {  }
        assertThat(fakeCurrencyLocalRepositoryImpl.dataInLocal, IsEqual(expectedList))
    }

    @Test
    fun useCase_forceUpdateAndRemoteError_remoteError() = runTest {
        val expectedDataState = DataState(
            DataStateStatus.Error,
            null,
            null,
            "No internet",
            DataStateErrorType.Remote
        )

        val listInLocal = createListOfRandomCurrencies()
        fakeCurrencyLocalRepositoryImpl.dataInLocal.addAll(listInLocal)
        fakeCurrencyRemoteRepositoryImpl.exception = IOException()

        val dataState = getAllCurrenciesUseCase(true).first()
        assertThat(dataState.status, IsEqual(expectedDataState.status))
        assertThat(dataState.data, IsEqual(expectedDataState.data))
        assertThat(dataState.isDataFromCache, IsEqual(expectedDataState.isDataFromCache))
        assertThat(dataState.errorMessage, IsEqual(expectedDataState.errorMessage))
        assertThat(dataState.errorType, IsEqual(expectedDataState.errorType))
    }

}