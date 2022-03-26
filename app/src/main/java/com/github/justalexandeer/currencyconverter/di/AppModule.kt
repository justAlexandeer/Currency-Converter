package com.github.justalexandeer.currencyconverter.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.github.justalexandeer.currencyconverter.business.data.local.abstraction.LastUpdateCurrencyLocalRepository
import com.github.justalexandeer.currencyconverter.business.interactors.GetAllCurrenciesUseCase
import com.github.justalexandeer.currencyconverter.business.interactors.GetLastUpdateCurrenciesUseCase
import com.github.justalexandeer.currencyconverter.business.interactors.SaveLastUpdateCurrenciesUseCase
import com.github.justalexandeer.currencyconverter.framework.datasource.local.database.AppDatabase
import com.github.justalexandeer.currencyconverter.framework.datasource.local.database.DATABASE_NAME
import com.github.justalexandeer.currencyconverter.framework.datasource.local.database.dao.CurrencyEntityDao
import com.github.justalexandeer.currencyconverter.framework.datasource.local.database.implementation.CurrencyLocalRepositoryImpl
import com.github.justalexandeer.currencyconverter.framework.datasource.local.preferences.SharedPreferencesManager
import com.github.justalexandeer.currencyconverter.framework.datasource.local.preferences.SharedPreferencesManager.Companion.APP_PREFERENCES
import com.github.justalexandeer.currencyconverter.framework.datasource.local.preferences.implementation.LastUpdateCurrencyLocalRepositoryImpl
import com.github.justalexandeer.currencyconverter.framework.datasource.remote.ApiCurrency
import com.github.justalexandeer.currencyconverter.framework.datasource.remote.ServiceCurrency
import com.github.justalexandeer.currencyconverter.framework.datasource.remote.implementation.CurrencyRemoteRepositoryImpl
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@Module
object AppModule {

    @Singleton
    @Provides
    fun provideCurrencyService(serviceCurrency: ServiceCurrency): ApiCurrency {
        return serviceCurrency.retrofit.create(ApiCurrency::class.java)
    }

    @Singleton
    @Provides
    fun provideCurrencyEntityDao(database: AppDatabase): CurrencyEntityDao {
        return database.currencyEntityDao()
    }

    @Singleton
    @Provides
    fun provideDatabase(applicationContext: Context): AppDatabase {
        return Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            DATABASE_NAME
        ).build()
    }

    @Singleton
    @Provides
    fun provideSharedPreferencesManager(
        sharedPreferences: SharedPreferences,
        sharedPreferencesEditor: SharedPreferences.Editor
    ): SharedPreferencesManager {
        return SharedPreferencesManager(sharedPreferences, sharedPreferencesEditor)
    }

    @Singleton
    @Provides
    fun provideSharedPreferences(applicationContext: Context): SharedPreferences {
        return applicationContext.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)
    }

    @Singleton
    @Provides
    fun provideSharedPreferencesEditor(sharedPreferences: SharedPreferences): SharedPreferences.Editor {
        return sharedPreferences.edit()
    }


    @Singleton
    @Provides
    fun provideGetAllCurrenciesUseCase(
        currencyLocalRepositoryImpl: CurrencyLocalRepositoryImpl,
        currencyRemoteRepositoryImpl: CurrencyRemoteRepositoryImpl,
        @IoDispatcher ioDispatcher: CoroutineDispatcher
    ) = GetAllCurrenciesUseCase(currencyLocalRepositoryImpl, currencyRemoteRepositoryImpl, ioDispatcher)

    @Singleton
    @Provides
    fun provideGetLastUpdateCurrenciesUseCase(
        lastUpdateCurrencyLocalRepositoryImpl: LastUpdateCurrencyLocalRepositoryImpl
    ) = GetLastUpdateCurrenciesUseCase(lastUpdateCurrencyLocalRepositoryImpl)


    @Singleton
    @Provides
    fun provideSaveLastUpdateCurrenciesUseCase(
        lastUpdateCurrencyLocalRepositoryImpl: LastUpdateCurrencyLocalRepositoryImpl
    ) = SaveLastUpdateCurrenciesUseCase(lastUpdateCurrencyLocalRepositoryImpl)
}
