package com.example.unscramble_game

import kotlinx.serialization.Serializable

sealed interface Routes {
    @Serializable
    object GamePanel

    @Serializable
    object GameHistory
}
