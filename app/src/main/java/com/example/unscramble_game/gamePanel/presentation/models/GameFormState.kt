package com.example.unscramble_game.gamePanel.presentation.models

import androidx.annotation.StringRes
import com.example.unscramble_game.core.domain.models.RoundGuess

data class GameFormState(
    val guess: RoundGuess = RoundGuess(text = ""),
    @StringRes
    val guessError: Int? = null,
)