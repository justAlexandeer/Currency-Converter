package com.github.justalexandeer.currencyconverter.framework.presentation.listcurrency.di

import androidx.lifecycle.ViewModel
import com.github.justalexandeer.currencyconverter.business.interactors.GetAllCurrenciesUseCase
import com.github.justalexandeer.currencyconverter.di.IoDispatcher
import com.github.justalexandeer.currencyconverter.di.ViewModelKey
import com.github.justalexandeer.currencyconverter.framework.datasource.local.database.implementation.CurrencyLocalRepositoryImpl
import com.github.justalexandeer.currencyconverter.framework.datasource.remote.implementation.CurrencyRemoteRepositoryImpl
import com.github.justalexandeer.currencyconverter.framework.presentation.listcurrency.CurrencyConverterViewModel
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@Module
abstract class CurrencyConverterModuleBinds {

    @Binds
    @IntoMap
    @ViewModelKey(CurrencyConverterViewModel::class)
    abstract fun bindViewModel(viewModel: CurrencyConverterViewModel): ViewModel

}