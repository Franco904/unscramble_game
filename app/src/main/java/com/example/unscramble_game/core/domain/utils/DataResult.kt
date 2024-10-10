package com.example.unscramble_game.core.domain.utils

sealed class DataResult<T>(val data: T?, val error: Throwable? = null) {
    class Loading<T>(data: T? = null) : DataResult<T>(data)
    class Success<T>(data: T) : DataResult<T>(data)
    class Error<T>(error: Throwable, data: T? = null) : DataResult<T>(data, error)
}
