package com.example.unscramble_game.previousGames.presentation

import com.example.unscramble_game.core.domain.models.GameTopic
import com.example.unscramble_game.previousGames.presentation.models.PreviousGameUiState
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentMapOf

object PreviousGamesDataSource {
    fun getPreviousGames() = persistentMapOf(
        "Today" to persistentListOf(
            PreviousGameUiState(
                topicText = GameTopic.MINECRAFT_MOBS.description,
                durationAndScoredRoundsText = "5m58s • Scored 8/10",
                totalScoreText = "80",
            ),
            PreviousGameUiState(
                topicText = GameTopic.BASKETBALL_TEAMS.description,
                durationAndScoredRoundsText = "1m5s • Scored 10/10",
                totalScoreText = "100",
            ),
        ),
        "Yesterday" to persistentListOf(
            PreviousGameUiState(
                topicText = GameTopic.COLOR_NAMES.description,
                durationAndScoredRoundsText = "9m34s • Scored 2/10",
                totalScoreText = "20",
            ),
        ),
        "July 15" to persistentListOf(
            PreviousGameUiState(
                topicText = GameTopic.HARRY_POTTER_SPELLS.description,
                durationAndScoredRoundsText = "46s • Scored 9/10",
                totalScoreText = "90",
            ),
            PreviousGameUiState(
                topicText = GameTopic.SPORT_NAMES.description,
                durationAndScoredRoundsText = "3m24s • Scored 4/10",
                totalScoreText = "40",
            ),
        ),
    )
}