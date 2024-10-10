package com.example.unscramble_game.gamePanel.presentation.models

import com.example.unscramble_game.core.domain.models.Topic

data class TopicUiState(val name: String? = null) {
    companion object {
        fun fromTopic(topic: Topic) = TopicUiState(name = topic.name)
    }
}
