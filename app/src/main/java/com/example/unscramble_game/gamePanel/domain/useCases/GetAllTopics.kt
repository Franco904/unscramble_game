package com.example.unscramble_game.gamePanel.domain.useCases

import com.example.unscramble_game.core.domain.models.Topic
import com.example.unscramble_game.core.domain.utils.DataResult
import com.example.unscramble_game.gamePanel.domain.GamePanelRepository

class GetAllTopics(
    private val repository: GamePanelRepository,
) {
    suspend operator fun invoke(): DataResult<List<Topic>> = repository.getAllTopics()
}
