package com.github.justalexandeer.currencyconverter.di

import com.github.justalexandeer.currencyconverter.business.data.local.abstraction.CurrencyLocalRepository
import com.github.justalexandeer.currencyconverter.business.data.local.abstraction.LastUpdateCurrencyLocalRepository
import com.github.justalexandeer.currencyconverter.business.data.remote.abstraction.CurrencyRemoteRepository
import com.github.justalexandeer.currencyconverter.business.interactors.GetLastUpdateCurrenciesUseCase
import com.github.justalexandeer.currencyconverter.business.interactors.SaveLastUpdateCurrenciesUseCase
import com.github.justalexandeer.currencyconverter.framework.datasource.local.database.implementation.CurrencyLocalRepositoryImpl
import com.github.justalexandeer.currencyconverter.framework.datasource.local.preferences.implementation.LastUpdateCurrencyLocalRepositoryImpl
import com.github.justalexandeer.currencyconverter.framework.datasource.remote.implementation.CurrencyRemoteRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
abstract class AppModuleBinds {

    @Binds
    abstract fun bindCurrencyLocalRepository(
        currencyLocalRepositoryImpl: CurrencyLocalRepositoryImpl
    ): CurrencyLocalRepository

    @Binds
    abstract fun bindCurrencyRemoteRepository(
        currencyRemoteRepositoryImpl: CurrencyRemoteRepositoryImpl
    ): CurrencyRemoteRepository

    abstract fun bindLastUpdateCurrencyLocalRepository(
        lastUpdateCurrencyLocalRepositoryImpl: LastUpdateCurrencyLocalRepositoryImpl
    ): LastUpdateCurrencyLocalRepository

}