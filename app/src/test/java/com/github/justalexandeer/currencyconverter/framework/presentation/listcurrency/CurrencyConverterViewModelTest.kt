package com.github.justalexandeer.currencyconverter.framework.presentation.listcurrency

import com.github.justalexandeer.currencyconverter.business.domain.model.CurrencyViewHolderState
import com.github.justalexandeer.currencyconverter.business.domain.state.SortType
import com.github.justalexandeer.currencyconverter.business.domain.util.createListOfRandomCurrencies
import com.github.justalexandeer.currencyconverter.business.domain.util.sortListCurrencyUtil
import com.github.justalexandeer.currencyconverter.business.interactors.GetAllCurrenciesUseCase
import com.github.justalexandeer.currencyconverter.business.interactors.GetLastUpdateCurrenciesUseCase
import com.github.justalexandeer.currencyconverter.business.interactors.SaveLastUpdateCurrenciesUseCase
import com.github.justalexandeer.currencyconverter.framework.datasource.local.database.implementation.fake.FakeCurrencyLocalRepositoryImpl
import com.github.justalexandeer.currencyconverter.framework.datasource.local.preferences.implementation.FakeLastUpdateCurrencyLocalRepositoryImpl
import com.github.justalexandeer.currencyconverter.framework.datasource.remote.implementation.fake.FakeCurrencyRemoteRepositoryImpl
import com.github.justalexandeer.currencyconverter.framework.presentation.listcurrency.model.CurrencyListScreenEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.*
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsEqual
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.io.IOException
import java.lang.Math.abs

@ExperimentalCoroutinesApi
class CurrencyConverterViewModelTest {
    private var fakeCurrencyLocalRepositoryImpl = FakeCurrencyLocalRepositoryImpl()
    private var fakeCurrencyRemoteRepositoryImpl = FakeCurrencyRemoteRepositoryImpl()
    private var fakeLastUpdateCurrencyLocalRepositoryImpl =
        FakeLastUpdateCurrencyLocalRepositoryImpl()
    private val getAllCurrenciesUseCase = GetAllCurrenciesUseCase(
        fakeCurrencyLocalRepositoryImpl,
        fakeCurrencyRemoteRepositoryImpl,
        Dispatchers.Unconfined
    )

    val dispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @After
    fun resetRepositories() {
        fakeLastUpdateCurrencyLocalRepositoryImpl = FakeLastUpdateCurrencyLocalRepositoryImpl()
        fakeCurrencyLocalRepositoryImpl = FakeCurrencyLocalRepositoryImpl()
        fakeCurrencyRemoteRepositoryImpl = FakeCurrencyRemoteRepositoryImpl()
    }

    private fun initViewModel(): CurrencyConverterViewModel {
        return CurrencyConverterViewModel(
            getAllCurrenciesUseCase,
            GetLastUpdateCurrenciesUseCase(fakeLastUpdateCurrencyLocalRepositoryImpl),
            SaveLastUpdateCurrenciesUseCase(fakeLastUpdateCurrencyLocalRepositoryImpl),
            Dispatchers.Unconfined
        )
    }

    @Test
    fun viewModel_loadDataFromLocalFirst_stateDataFromLocal() = runTest {
        val dataInLocal = createListOfRandomCurrencies()
        val expectedListCurrencyViewHolderState = sortListCurrencyUtil(
            SortType.AlphabeticalAsc,
            dataInLocal
        ).map { CurrencyViewHolderState(it, false) }

        fakeCurrencyLocalRepositoryImpl.dataInLocal.addAll(dataInLocal)
        val viewModel = initViewModel()

        val result = viewModel.currencyListScreenState.drop(1).first()
        assertThat(result.data, IsEqual(expectedListCurrencyViewHolderState))
    }

    @Test
    fun viewModel_loadDataFromRemote_stateDataFromRemote() = runTest {
        val dataInRemote = createListOfRandomCurrencies()
        val expectedListCurrencyViewHolderState = sortListCurrencyUtil(
            SortType.AlphabeticalAsc,
            dataInRemote
        ).map { CurrencyViewHolderState(it, false) }

        fakeCurrencyRemoteRepositoryImpl.dataFromRemote.addAll(dataInRemote)
        val viewModel = initViewModel()

        val result = viewModel.currencyListScreenState.drop(1).first()
        assertThat(result.data, IsEqual(expectedListCurrencyViewHolderState))
    }

    @Test
    fun viewModel_loadDataFromRemoteAndForceUpdateWithNewData_stateUpdateRemoteData() = runTest {
        // viewModel load data from remote
        val dataInRemote = createListOfRandomCurrencies()
        fakeCurrencyRemoteRepositoryImpl.dataFromRemote.addAll(dataInRemote)
        val viewModel = initViewModel()

        val updateDataInRemote = createListOfRandomCurrencies()
        val expectedListCurrencyViewHolderState = sortListCurrencyUtil(
            SortType.AlphabeticalAsc,
            updateDataInRemote
        ).map { CurrencyViewHolderState(it, false) }

        // user update data with new values from remote
        fakeCurrencyRemoteRepositoryImpl.dataFromRemote.clear()
        fakeCurrencyRemoteRepositoryImpl.dataFromRemote.addAll(updateDataInRemote)
        viewModel.obtainEvent(CurrencyListScreenEvent.ForceUpdate)

        val result = viewModel.currencyListScreenState.drop(1).first()
        assertThat(result.data, IsEqual(expectedListCurrencyViewHolderState))
    }

    @Test
    fun viewModel_chooseCurrency_stateWithChosenCurrency() = runTest {
        // viewModel load data from remote
        val dataInRemote = createListOfRandomCurrencies()
        val sortedDataInRemote = sortListCurrencyUtil(
            SortType.AlphabeticalAsc,
            dataInRemote
        )
        fakeCurrencyRemoteRepositoryImpl.dataFromRemote.addAll(dataInRemote)
        val viewModel = initViewModel()
        viewModel.currencyListScreenState.drop(1).first()

        // user click on currency
        viewModel.obtainEvent(CurrencyListScreenEvent.OnCurrencyClick(sortedDataInRemote[1]))

        val expectedList = sortedDataInRemote.mapIndexed { index, currency ->
            if (index == 1) {
                CurrencyViewHolderState(currency, true)
            } else {
                CurrencyViewHolderState(currency, false)
            }
        }
        val expectedChosenCurrency = expectedList[1].currency
        val result = viewModel.currencyListScreenState.first()
        assertThat(result.data, IsEqual(expectedList))
        assertThat(result.chosenCurrency, IsEqual(expectedChosenCurrency))
    }

    @Test
    fun viewModel_chooseCurrencyUpdateFromRemote_stateWithUpdatedChosenCurrency() = runTest {
        // viewModel load data from remote
        val dataInRemote = createListOfRandomCurrencies()
        val sortedDataInRemote = sortListCurrencyUtil(
            SortType.AlphabeticalAsc,
            dataInRemote
        ).toMutableList()
        fakeCurrencyRemoteRepositoryImpl.dataFromRemote.addAll(dataInRemote)
        val viewModel = initViewModel()
        viewModel.currencyListScreenState.drop(1).first()

        // user click on currency
        viewModel.obtainEvent(CurrencyListScreenEvent.OnCurrencyClick(sortedDataInRemote[1]))

        // user update data with new values from remote
        val updatedCurrency = sortedDataInRemote[1].copy(value = 100.0)
        sortedDataInRemote[1] = updatedCurrency
        fakeCurrencyRemoteRepositoryImpl.dataFromRemote.clear()
        fakeCurrencyRemoteRepositoryImpl.dataFromRemote.addAll(sortedDataInRemote)
        viewModel.obtainEvent(CurrencyListScreenEvent.ForceUpdate)

        val expectedList = sortedDataInRemote.mapIndexed { index, currency ->
            if (index == 1) {
                CurrencyViewHolderState(currency, true)
            } else {
                CurrencyViewHolderState(currency, false)
            }
        }
        val expectedChosenCurrency = expectedList[1].currency
        val result = viewModel.currencyListScreenState.drop(1).first()

        assertThat(result.data, IsEqual(expectedList))
        assertThat(result.chosenCurrency, IsEqual(expectedChosenCurrency))
    }

    @Test
    fun viewModel_chooseCurrencyAndSetValueToEditText_stateWithConvertResult() = runTest {
        // viewModel load data from remote
        val dataInRemote = createListOfRandomCurrencies()
        val sortedDataInRemote = sortListCurrencyUtil(
            SortType.AlphabeticalAsc,
            dataInRemote
        )
        fakeCurrencyRemoteRepositoryImpl.dataFromRemote.addAll(dataInRemote)
        val viewModel = initViewModel()
        viewModel.currencyListScreenState.drop(1).first()

        // user click on currency
        viewModel.obtainEvent(CurrencyListScreenEvent.OnCurrencyClick(sortedDataInRemote[1]))

        // user set value to edit text
        val valueFromEditText = 100.0
        viewModel.obtainEvent(CurrencyListScreenEvent.ConvertValueEditTextChange(valueFromEditText.toString()))

        val expectedList = sortedDataInRemote.mapIndexed { index, currency ->
            if (index == 1) {
                CurrencyViewHolderState(currency, true)
            } else {
                CurrencyViewHolderState(currency, false)
            }
        }
        val result = viewModel.currencyListScreenState.first().convertResult
        val convertValue =
            (valueFromEditText / (expectedList[1].currency.value / expectedList[1].currency.nominal))
        val expectedResult = abs(convertValue - result!!.toDouble()) < 0.0001
        assertThat(true, IsEqual(expectedResult))
    }

    @Test
    fun viewModel_chosenCurrencyAndConvertValueAndUpdateFromRemoteCurrency_stateWithUpdateConvertResult() = runTest{
        // viewModel get data from Remote
        val dataInRemote = createListOfRandomCurrencies()
        val sortedDataInRemote = sortListCurrencyUtil(
            SortType.AlphabeticalAsc,
            dataInRemote
        ).toMutableList()

        // user click onCurrency
        fakeCurrencyRemoteRepositoryImpl.dataFromRemote.addAll(dataInRemote)
        val viewModel = initViewModel()
        viewModel.currencyListScreenState.drop(1).first()
        viewModel.obtainEvent(CurrencyListScreenEvent.OnCurrencyClick(sortedDataInRemote[1]))

        // user set value to edit text
        val valueFromEditText = 100.0
        viewModel.obtainEvent(CurrencyListScreenEvent.ConvertValueEditTextChange(valueFromEditText.toString()))

        // user updateList
        val updatedCurrency = sortedDataInRemote[1].copy(value = 1000.0)
        sortedDataInRemote[1] = updatedCurrency
        fakeCurrencyRemoteRepositoryImpl.dataFromRemote.clear()
        fakeCurrencyRemoteRepositoryImpl.dataFromRemote.addAll(sortedDataInRemote)
        viewModel.obtainEvent(CurrencyListScreenEvent.ForceUpdate)
        viewModel.currencyListScreenState.drop(1).first()

        val expectedList = sortedDataInRemote.mapIndexed { index, currency ->
            if (index == 1) {
                CurrencyViewHolderState(currency, true)
            } else {
                CurrencyViewHolderState(currency, false)
            }
        }

        val result = viewModel.currencyListScreenState.value.convertResult
        val convertValue =
            (valueFromEditText / (expectedList[1].currency.value / expectedList[1].currency.nominal))
         val expectedResult = abs(convertValue - result!!.toDouble()) < 0.0001
        assertThat(true, IsEqual(expectedResult))
    }

    @Test
    fun viewModel_localHaveDataAndInLocalError_dataFromLocalAndErrorMessage() = runTest {
        // viewModel load data from local
        val dataInLocal = createListOfRandomCurrencies()
        val expectedListCurrencyViewHolderState = sortListCurrencyUtil(
            SortType.AlphabeticalAsc,
            dataInLocal
        ).map { CurrencyViewHolderState(it, false) }

        fakeCurrencyLocalRepositoryImpl.dataInLocal.addAll(dataInLocal)
        fakeCurrencyRemoteRepositoryImpl.exception = IOException()
        val viewModel = initViewModel()


        val stateResult = viewModel.currencyListScreenState.drop(1).first()
        assertThat(stateResult.data, IsEqual(expectedListCurrencyViewHolderState))
        val errorMessage = viewModel.error.first()
        assertThat(errorMessage, IsEqual("No internet"))
    }

}