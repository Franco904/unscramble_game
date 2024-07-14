package com.example.unscramble_game.core.domain.validation

sealed class ValidationResult(val error: String? = null) {
    class Success : ValidationResult()
    class Error(error: String) : ValidationResult(error = error)
}
