package com.github.justalexandeer.currencyconverter.framework.presentation

import android.app.Application
import com.github.justalexandeer.currencyconverter.di.AppComponent
import com.github.justalexandeer.currencyconverter.di.AppModule
import com.github.justalexandeer.currencyconverter.di.DaggerAppComponent

class CurrencyConverterApplication: Application() {

    val appComponent: AppComponent by lazy {
        DaggerAppComponent.factory().create(applicationContext)
    }

}