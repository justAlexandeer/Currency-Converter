package com.github.justalexandeer.currencyconverter.business.domain.state

import android.os.Parcelable

sealed class SortType {
    object AlphabeticalAsc : SortType()
    object AlphabeticalDesc : SortType()
    object ValueAsc : SortType()
    object ValueDesc : SortType()
}