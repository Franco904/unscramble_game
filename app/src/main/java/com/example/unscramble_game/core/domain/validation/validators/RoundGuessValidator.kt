package com.example.unscramble_game.core.domain.validation.validators

import com.example.unscramble_game.core.domain.validation.GUESS_IS_REQUIRED
import com.example.unscramble_game.core.domain.validation.ValidationResult

val roundGuessValidator = InputFieldValidator<String> { roundGuess ->
    when {
        roundGuess.isBlank() -> ValidationResult.Error(GUESS_IS_REQUIRED)
        else -> ValidationResult.Success
    }
}
