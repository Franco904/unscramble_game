package com.example.unscramble_game.core.domain.utils

sealed class DataEvent<T>(val data: T?, val error: Throwable? = null) {
    class Loading<T>(data: T? = null) : DataEvent<T>(data)
    class Success<T>(data: T) : DataEvent<T>(data)
    class Error<T>(error: Throwable, data: T? = null) : DataEvent<T>(data, error)
}
