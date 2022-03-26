package com.github.justalexandeer.currencyconverter.framework.datasource.local.preferences

import android.content.SharedPreferences
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SharedPreferencesManager @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val sharedPreferencesEditor: SharedPreferences.Editor
) {

    fun saveLastUpdateDate(date: Long) {
        with(sharedPreferencesEditor) {
            putLong(LAST_UPDATE_DATE, date)
            apply()
        }
    }

    fun getLastUpdateDate(): Long? {
        val date = sharedPreferences.getLong(LAST_UPDATE_DATE, 0L)
        return if(date == 0L)
            null
        else
            date
    }

    companion object {
        const val APP_PREFERENCES = "AppPreferences"
        private const val LAST_UPDATE_DATE = "LastUpdateDate"
    }
}