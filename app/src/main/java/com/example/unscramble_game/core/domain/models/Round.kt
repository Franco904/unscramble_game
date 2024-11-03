package com.example.unscramble_game.core.domain.models

data class Round(
    val id: Long? = null,
    val game: Game,
    val word: Word,
    val number: Int,
    val scrambledWord: String,
    val guess: String? = null,
    val isScored: Boolean = false,
    val isSkipped: Boolean = false,
)
