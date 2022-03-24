package com.github.justalexandeer.currencyconverter.framework.presentation.listcurrency.di

import androidx.lifecycle.ViewModel
import com.github.justalexandeer.currencyconverter.di.ViewModelKey
import com.github.justalexandeer.currencyconverter.framework.presentation.listcurrency.CurrencyConverterViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class CurrencyConverterModule {

    @Binds
    @IntoMap
    @ViewModelKey(CurrencyConverterViewModel::class)
    abstract fun bindViewModel(viewModel: CurrencyConverterViewModel): ViewModel
}