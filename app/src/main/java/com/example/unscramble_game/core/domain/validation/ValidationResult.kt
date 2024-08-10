package com.example.unscramble_game.core.domain.validation

sealed class ValidationResult(val error: String? = null) {
    data object Success : ValidationResult()
    class Error(error: String) : ValidationResult(error = error)
}
