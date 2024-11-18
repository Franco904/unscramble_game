package com.example.unscramble_game.previousGames.presentation.models

import com.example.unscramble_game.core.data.local.database.entities.intermediates.GameWithTopicAndScoredRoundsCount
import kotlin.math.roundToInt

data class PreviousGameUiState(
    val gameId: Long? = null,
    val topicText: String? = null,
    val durationAndScoredRoundsText: String? = null,
    val totalScoreText: String? = null,
) {
    companion object {
        fun fromGameData(
            game: GameWithTopicAndScoredRoundsCount,
        ): PreviousGameUiState {
            val gameDuration = game.gameEndTimestamp - game.gameStartTimestamp
            val seconds = (gameDuration / 1000f).roundToInt()

            val hours = (seconds / 3600f).roundToInt()
            val hoursText = if (hours >= 1) "${hours}h" else ""

            val minutes = ((seconds % 3600) / 60f).roundToInt()
            val minutesText = if (minutes >= 1) "${minutes}m" else ""

            val remainingSeconds = seconds % 60

            return PreviousGameUiState(
                gameId = game.gameId,
                topicText = game.topicName,
                durationAndScoredRoundsText =
                "$hoursText$minutesText${remainingSeconds}s • Scored ${game.scoredRoundsCount}/10",
                totalScoreText = (game.scoredRoundsCount * 10).toString(),
            )
        }
    }
}