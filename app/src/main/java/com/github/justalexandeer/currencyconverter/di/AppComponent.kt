package com.github.justalexandeer.currencyconverter.di

import android.content.Context
import com.github.justalexandeer.currencyconverter.framework.presentation.listcurrency.di.CurrencyConverterComponent
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import javax.inject.Singleton

@Singleton
@Component (
    modules = [
        AppModule::class,
        AppModuleBinds::class,
        ViewModelBuilderModule::class,
        SubcomponentModule::class
    ]
)
interface AppComponent {

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance applicationContext: Context
        ): AppComponent
    }

    fun currencyConverterComponent(): CurrencyConverterComponent.Factory

}

@Module(
    subcomponents = []
)
object SubcomponentModule