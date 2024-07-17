package com.example.unscramble_game.gamePanel.presentation.models

import android.os.Parcelable
import androidx.annotation.StringRes
import com.example.unscramble_game.core.domain.models.GameTopic
import kotlinx.parcelize.Parcelize

@Parcelize
data class GameControlState(
    val gameState: GameState = GameState.NOT_STARTED,
    val topic: GameTopic? = null,
    val totalScore: Int = 0,
    val round: Int = 1,
    val scrambledRoundWord: String? = null,
    @StringRes
    val primaryButtonText: Int? = null,
    @StringRes
    val secondaryButtonText: Int? = null,
) : Parcelable
