package com.github.justalexandeer.currencyconverter.business.interactors

import com.github.justalexandeer.currencyconverter.business.data.local.abstraction.LastUpdateCurrencyLocalRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetLastUpdateCurrenciesUseCase @Inject constructor(
    private val lastUpdateCurrencyLocalRepository: LastUpdateCurrencyLocalRepository
) {

    operator fun invoke(): Long? = lastUpdateCurrencyLocalRepository.getLastUpdateDate()

}