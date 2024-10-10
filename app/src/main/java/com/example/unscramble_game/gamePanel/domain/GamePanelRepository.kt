package com.example.unscramble_game.gamePanel.domain

import com.example.unscramble_game.core.domain.models.Topic
import com.example.unscramble_game.core.domain.utils.DataResult

interface GamePanelRepository {
    suspend fun getAllTopics(): DataResult<List<Topic>>
}
