package com.example.unscramble_game.gamePanel.presentation.states

import androidx.annotation.StringRes
import com.example.unscramble_game.gamePanel.domain.models.Guess

data class GameFormState(
    val guess: Guess = Guess(text = ""),
    @StringRes
    val guessError: Int? = null,
)