package com.github.justalexandeer.currencyconverter.framework.presentation.listcurrency.di

import com.github.justalexandeer.currencyconverter.framework.presentation.listcurrency.CurrencyConverterFragment
import dagger.Subcomponent

@Subcomponent(modules = [CurrencyConverterModule::class])
interface CurrencyConverterComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): CurrencyConverterComponent
    }

    fun inject(fragment: CurrencyConverterFragment)

}