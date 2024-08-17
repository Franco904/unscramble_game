package com.example.unscramble_game.gamePanel.presentation.models

import androidx.annotation.StringRes
import com.example.unscramble_game.core.domain.models.GameTopic

data class GameControlUiState(
    val gameStatus: GameStatus = GameStatus.NotStarted,
    val topic: GameTopic? = null,
    val totalScore: Int = 0,
    val round: Int = 1,
    val scrambledRoundWord: String? = null,
    @StringRes
    val primaryButtonText: Int? = null,
    @StringRes
    val secondaryButtonText: Int? = null,
)
