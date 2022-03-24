package com.github.justalexandeer.currencyconverter.di

import com.github.justalexandeer.currencyconverter.business.data.local.abstraction.CurrencyLocalRepository
import com.github.justalexandeer.currencyconverter.business.data.remote.abstraction.CurrencyRemoteRepository
import com.github.justalexandeer.currencyconverter.framework.datasource.local.database.implementation.CurrencyLocalRepositoryImpl
import com.github.justalexandeer.currencyconverter.framework.datasource.remote.implementation.CurrencyRemoteRepositoryImpl
import dagger.Binds
import dagger.Module
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

}