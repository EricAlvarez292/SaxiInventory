package com.example.saxiinventory.common

import retrofit2.HttpException
import java.io.IOException
import java.net.HttpURLConnection


fun Throwable.toCustomNetworkError(): CustomNetworkError {
    return when (this) {
        is HttpException -> {
            when (this.code()) {
                HttpURLConnection.HTTP_GATEWAY_TIMEOUT -> CustomNetworkError.GATEWAY_TIMEOUT
                HttpURLConnection.HTTP_CLIENT_TIMEOUT -> CustomNetworkError.REQUEST_TIMEOUT
                else -> CustomNetworkError.UNKNOWN
            }
        }

        is IOException -> CustomNetworkError.NO_INTERNET
        else -> CustomNetworkError.UNKNOWN
    }
}