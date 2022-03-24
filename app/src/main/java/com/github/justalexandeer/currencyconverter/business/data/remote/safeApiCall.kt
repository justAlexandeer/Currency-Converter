package com.github.justalexandeer.currencyconverter.business.data.remote

import com.github.justalexandeer.currencyconverter.business.domain.state.DataState
import com.github.justalexandeer.currencyconverter.business.domain.state.DataStateErrorType
import com.github.justalexandeer.currencyconverter.business.domain.state.DataStateStatus
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import retrofit2.HttpException
import java.io.IOException

suspend fun <T> safeApiCall(
    dispatcher: CoroutineDispatcher,
    apiCall: suspend () -> T,
): DataState<T> {
    return withContext(dispatcher) {
        try {
            withTimeout(NETWORK_TIMEOUT) {
                DataState(DataStateStatus.Success, apiCall.invoke(), null, null)
            }
        } catch (throwable: Throwable) {
            //Log.i(TAG, "${throwable.printStackTrace()}")
            when (throwable) {
                is TimeoutCancellationException -> {
                    DataState(
                        DataStateStatus.Error,
                        null,
                        NETWORK_ERROR_TIMEOUT,
                        DataStateErrorType.Remote
                    )
                }
                is IOException -> {
                    DataState(
                        DataStateStatus.Error,
                        null,
                        NETWORK_ERROR_NO_INTERNET,
                        DataStateErrorType.Remote
                    )
                }
                is HttpException -> {
                    DataState(
                        DataStateStatus.Error,
                        null,
                        throwable.localizedMessage,
                        DataStateErrorType.Remote
                    )
                }
                else -> {
                    DataState(
                        DataStateStatus.Error,
                        null,
                        NETWORK_ERROR_UNKNOWN,
                        DataStateErrorType.Remote
                    )
                }
            }
        }
    }
}

private const val TAG = "SafeApiCall"

const val NETWORK_TIMEOUT = 6000L

const val NETWORK_ERROR_UNKNOWN = "Network unknown problem"
const val NETWORK_ERROR_TIMEOUT = "Network timeout"
const val NETWORK_ERROR_NO_INTERNET = "No internet"