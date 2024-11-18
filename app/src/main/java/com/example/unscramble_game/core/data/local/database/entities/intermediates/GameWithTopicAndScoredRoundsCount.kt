package com.example.unscramble_game.core.data.local.database.entities.intermediates

data class GameWithTopicAndScoredRoundsCount(
    val gameId: Long,
    val topicName: String,
    val gameStartTimestamp: Long,
    val gameEndTimestamp: Long,
    val scoredRoundsCount: Int,
)
