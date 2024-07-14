package com.example.unscramble_game.gamePanel.domain.models

import com.example.unscramble_game.core.domain.validation.GUESS_IS_REQUIRED
import com.example.unscramble_game.core.domain.validation.Validatable
import com.example.unscramble_game.core.domain.validation.ValidationResult

data class Guess(val text: String): Validatable {
    override fun validate() = when {
        text.isBlank() -> ValidationResult.Error(GUESS_IS_REQUIRED)
        else -> ValidationResult.Success()
    }
}
