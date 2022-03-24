package com.github.justalexandeer.currencyconverter.framework.datasource.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.github.justalexandeer.currencyconverter.framework.datasource.local.database.entity.CurrencyEntity

@Dao
interface CurrencyEntityDao {

    @Query("SELECT * FROM currency_entity")
    suspend fun getAllCurrencies(): List<CurrencyEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveListOfCurrencyEntity(listOfCurrencyEntity: List<CurrencyEntity>)

}