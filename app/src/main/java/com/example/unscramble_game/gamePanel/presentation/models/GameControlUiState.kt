package com.example.unscramble_game.gamePanel.presentation.models

import androidx.annotation.StringRes

data class GameControlUiState(
    val gameStatus: GameStatus = GameStatus.NotStarted,
    val topicName: String? = null,
    val totalScore: Int = 0,
    val round: Int = 1,
    val scrambledRoundWord: String? = null,
    @StringRes
    val primaryButtonText: Int? = null,
    @StringRes
    val secondaryButtonText: Int? = null,
)
