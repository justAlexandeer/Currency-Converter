package com.github.justalexandeer.currencyconverter.framework.datasource.local.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = tableName)
data class CurrencyEntity(
    @PrimaryKey
    val id: String,
    val charCode: String,
    val name: String,
    val nominal: Int,
    val numCode: String,
    val previous: Double,
    val value: Double
)

private const val tableName = "currency_entity"