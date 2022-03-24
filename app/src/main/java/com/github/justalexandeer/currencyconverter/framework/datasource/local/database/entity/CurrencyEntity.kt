package com.github.justalexandeer.currencyconverter.framework.datasource.local.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = tableName)
data class CurrencyEntity(
    @PrimaryKey(autoGenerate = true)
    val tableId: Long = 0L,
    val charCode: String,
    val id: String,
    val name: String,
    val nominal: Int,
    val numCode: String,
    val previous: Double,
    val value: Double
)

private const val tableName = "currency_entity"