package com.example.unscramble_game.core.domain.validation.validators

import com.example.unscramble_game.core.domain.validation.ValidationResult

fun interface InputFieldValidator<T> {
    fun validate(value: T): ValidationResult
}
