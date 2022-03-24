package com.github.justalexandeer.currencyconverter.business.domain.state

data class DataState<T>(
    val status: DataStateStatus,
    val data: T?,
    val errorMessage: String?,
    val errorType: DataStateErrorType?
)

sealed class DataStateStatus {
    object Success : DataStateStatus()
    object Error : DataStateStatus()
}

sealed class DataStateErrorType {
    object Local: DataStateErrorType()
    object Remote: DataStateErrorType()
    object Business: DataStateErrorType()
}