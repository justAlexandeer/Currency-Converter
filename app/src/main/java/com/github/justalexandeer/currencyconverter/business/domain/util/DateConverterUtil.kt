package com.github.justalexandeer.currencyconverter.business.domain.util

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SimpleDateFormat")
fun fromLongToDate(date: Long): String {
    val dateFormat = SimpleDateFormat("HH:mm:ss 'in' dd.MM.yyyy")
    return dateFormat.format(Date(date))
}