package com.example.unscramble_game.core.domain.validation

import com.example.unscramble_game.core.domain.validation.validators.InputFieldValidator

data class InputField<T>(
    val value: T,
    private val validator: InputFieldValidator<T>,
) {
    fun validate() = validator.validate(value)
}
