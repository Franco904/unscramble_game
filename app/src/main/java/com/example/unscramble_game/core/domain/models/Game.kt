package com.example.unscramble_game.core.domain.models

data class Game(
    val id: Long? = null,
    val topic: Topic,
    val startTimestamp: Long,
    val endTimestamp: Long? = null,
    val rounds: List<Round> = emptyList(),
)
