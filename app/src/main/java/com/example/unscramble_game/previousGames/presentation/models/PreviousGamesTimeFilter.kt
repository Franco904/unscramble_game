package com.example.unscramble_game.previousGames.presentation.models

enum class PreviousGamesTimeFilter(val timeAgo: Int) {
    TODAY(timeAgo = 0),
    LAST_7_DAYS(timeAgo = -7),
    LAST_30_DAYS(timeAgo = -30),
    LAST_90_DAYS(timeAgo = -90),
}