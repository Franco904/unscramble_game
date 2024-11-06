package com.example.unscramble_game

import kotlinx.serialization.Serializable

sealed interface Routes {
    @Serializable
    data object GamePanel

    @Serializable
    data object PreviousGames

    @Serializable
    data object CidadaoConditionCardDraft
}
