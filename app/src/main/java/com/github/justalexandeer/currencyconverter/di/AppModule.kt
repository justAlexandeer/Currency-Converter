package com.github.justalexandeer.currencyconverter.di

import android.content.Context
import androidx.room.Room
import com.github.justalexandeer.currencyconverter.framework.datasource.local.database.AppDatabase
import com.github.justalexandeer.currencyconverter.framework.datasource.local.database.DATABASE_NAME
import com.github.justalexandeer.currencyconverter.framework.datasource.local.database.dao.CurrencyEntityDao
import com.github.justalexandeer.currencyconverter.framework.datasource.remote.ApiCurrency
import com.github.justalexandeer.currencyconverter.framework.datasource.remote.ServiceCurrency
import dagger.Module
import dagger.Provides
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

}
