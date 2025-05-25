package com.example.saxiinventory.common


typealias RootError = Error

sealed interface Result<out D, out E : RootError> {
    data object Loading : Result<Nothing, Nothing>
    data class Success<out D, out E : RootError>(val data: D) : Result<D, E>
    data class Error<out D, out E : RootError>(val error: E, val data: String? = null) :
        Result<D, E>
}

sealed interface Error

enum class CustomNetworkError : Error {
    GATEWAY_TIMEOUT,
    REQUEST_TIMEOUT,
    NO_INTERNET,
    SERVER_ERROR,
    UNKNOWN
}