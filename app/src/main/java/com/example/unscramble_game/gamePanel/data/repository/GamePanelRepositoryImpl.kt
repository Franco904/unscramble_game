package com.example.unscramble_game.gamePanel.data.repository

import com.example.unscramble_game.core.data.local.daos.TopicDao
import com.example.unscramble_game.core.domain.models.Topic
import com.example.unscramble_game.core.domain.utils.DataResult
import com.example.unscramble_game.gamePanel.domain.GamePanelRepository

class GamePanelRepositoryImpl(
    private val topicDao: TopicDao,
) : GamePanelRepository {
    override suspend fun getAllTopics(): DataResult<List<Topic>> {
        return try {
            val topics = topicDao.getAll().map { it.toTopic() }

            DataResult.Success(data = topics)
        } catch (e: Exception) {
            DataResult.Error(error = e)
        }
    }
}
