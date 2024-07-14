package com.example.unscramble_game.core.domain.validation

sealed class ValidationResult(val isSuccessful: Boolean, val error: String? = null) {
    class Success : ValidationResult(isSuccessful = true)
    class Error(error: String) : ValidationResult(isSuccessful = false, error = error)
}
