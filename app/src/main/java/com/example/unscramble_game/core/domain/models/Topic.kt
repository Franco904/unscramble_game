package com.example.unscramble_game.core.domain.models

data class Topic(
    val id: Long,
    val name: String,
    val words: List<Word>,
)
