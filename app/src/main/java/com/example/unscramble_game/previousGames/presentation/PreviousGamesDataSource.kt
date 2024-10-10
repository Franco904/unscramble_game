package com.example.unscramble_game.previousGames.presentation

import com.example.unscramble_game.previousGames.presentation.models.PreviousGameUiState
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentMapOf

object PreviousGamesDataSource {
    fun getPreviousGames() = persistentMapOf(
        "Today" to persistentListOf(
            PreviousGameUiState(
                topicText = "Minecraft mobs",
                durationAndScoredRoundsText = "5m58s • Scored 8/10",
                totalScoreText = "80",
            ),
            PreviousGameUiState(
                topicText = "Basketball teams - NBA",
                durationAndScoredRoundsText = "1m5s • Scored 10/10",
                totalScoreText = "100",
            ),
        ),
        "Yesterday" to persistentListOf(
            PreviousGameUiState(
                topicText = "Colors",
                durationAndScoredRoundsText = "9m34s • Scored 2/10",
                totalScoreText = "20",
            ),
        ),
        "July 15" to persistentListOf(
            PreviousGameUiState(
                topicText = "Harry Potter spells",
                durationAndScoredRoundsText = "46s • Scored 9/10",
                totalScoreText = "90",
            ),
            PreviousGameUiState(
                topicText = "Sports",
                durationAndScoredRoundsText = "3m24s • Scored 4/10",
                totalScoreText = "40",
            ),
        ),
    )
}
