package com.example.unscramble_game.previousGames.presentation

import com.example.unscramble_game.core.domain.models.GameTopic
import com.example.unscramble_game.previousGames.presentation.models.PreviousGameState
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentMapOf

object PreviousGamesDataSource {
    fun getPreviousGames() = persistentMapOf(
        "Today" to persistentListOf(
            PreviousGameState(
                topicText = GameTopic.MINECRAFT_MOBS.description,
                durationAndScoredRoundsText = "5m58s • Scored 8/10",
                totalScoreText = "80",
            ),
            PreviousGameState(
                topicText = GameTopic.BASKETBALL_TEAMS.description,
                durationAndScoredRoundsText = "1m5s • Scored 10/10",
                totalScoreText = "100",
            ),
        ),
        "Yesterday" to persistentListOf(
            PreviousGameState(
                topicText = GameTopic.COLOR_NAMES.description,
                durationAndScoredRoundsText = "9m34s • Scored 2/10",
                totalScoreText = "20",
            ),
        ),
        "July 15" to persistentListOf(
            PreviousGameState(
                topicText = GameTopic.HARRY_POTTER_SPELLS.description,
                durationAndScoredRoundsText = "46s • Scored 9/10",
                totalScoreText = "90",
            ),
            PreviousGameState(
                topicText = GameTopic.SPORT_NAMES.description,
                durationAndScoredRoundsText = "3m24s • Scored 4/10",
                totalScoreText = "40",
            ),
        ),
    )
}