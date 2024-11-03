package com.example.unscramble_game.gamePanel.data.repository

import com.example.unscramble_game.core.data.local.database.daos.GameDao
import com.example.unscramble_game.core.data.local.database.daos.RoundDao
import com.example.unscramble_game.core.data.local.database.daos.TopicDao
import com.example.unscramble_game.core.data.local.database.entities.GameEntity
import com.example.unscramble_game.core.data.local.database.entities.RoundEntity
import com.example.unscramble_game.core.domain.models.Game
import com.example.unscramble_game.core.domain.models.Topic
import com.example.unscramble_game.gamePanel.domain.GamePanelRepository

class GamePanelRepositoryImpl(
    private val gameDao: GameDao,
    private val roundDao: RoundDao,
    private val topicDao: TopicDao,
) : GamePanelRepository {
    override suspend fun getAllTopics(): List<Topic> {
        return topicDao.getAllWithWords().map { topicWithWords ->
            Topic(
                id = topicWithWords.topic.id,
                name = topicWithWords.topic.name,
                words = topicWithWords.words.map { it.toWord() },
            )
        }
    }

    override suspend fun saveGame(game: Game) {
        val gameId = gameDao.insert(GameEntity.fromGame(game))

        game.rounds.forEach { round ->
            roundDao.insert(RoundEntity.fromRound(gameId, round))
        }
    }
}
