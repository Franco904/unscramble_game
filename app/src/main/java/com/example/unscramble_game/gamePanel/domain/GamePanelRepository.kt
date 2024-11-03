package com.example.unscramble_game.gamePanel.domain

import com.example.unscramble_game.core.domain.models.Game
import com.example.unscramble_game.core.domain.models.Topic

interface GamePanelRepository {
    suspend fun getAllTopics(): List<Topic>

    suspend fun saveGame(game: Game)
}
