package com.example.unscramble_game.gamePanel.presentation.models

import androidx.annotation.StringRes
import com.example.unscramble_game.core.domain.validation.InputField
import com.example.unscramble_game.core.domain.validation.validators.roundGuessValidator

data class GameFormUiState(
    val guess: InputField<String> = InputField("", validator = roundGuessValidator),
    @StringRes
    val guessError: Int? = null,
)