package com.example.unscramble_game.gamePanel.presentation.models

import androidx.annotation.StringRes
import com.example.unscramble_game.core.domain.validation.InputField
import com.example.unscramble_game.core.domain.validation.validators.roundGuessValidator

data class GameFormState(
    val guess: InputField<String> = InputField(value = "", validator = roundGuessValidator),
    @StringRes
    val guessError: Int? = null,
)