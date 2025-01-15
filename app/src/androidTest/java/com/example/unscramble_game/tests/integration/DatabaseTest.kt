package com.example.unscramble_game.tests.integration

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import com.example.unscramble_game.core.data.local.database.UnscrambleGameDatabase
import com.example.unscramble_game.core.data.local.database.entities.GameEntity
import com.example.unscramble_game.core.data.local.database.entities.RoundEntity
import com.example.unscramble_game.core.data.local.database.entities.TopicEntity
import com.example.unscramble_game.core.data.local.database.entities.intermediates.GameWithTopicAndScoredRoundsCount
import com.example.unscramble_game.core.domain.models.Game
import com.example.unscramble_game.core.domain.models.Round
import com.example.unscramble_game.core.domain.models.Topic
import com.example.unscramble_game.core.domain.models.Word
import com.example.unscramble_game.core.presentation.utils.scramble
import io.github.serpro69.kfaker.Faker
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.test.runTest
import org.amshove.kluent.shouldBeEqualTo
import org.junit.After
import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.inject

class DatabaseTest : KoinTest {
    private val sut: UnscrambleGameDatabase by inject()

    @After
    fun after() {
        sut.close()
    }

    @Test
    fun mustSaveGameInDatabase() = runTest {
        val db = sut.openHelper.writableDatabase
        val gameDao = sut.gameDao()
        val roundDao = sut.roundDao()

        val topic = TopicEntity(
            id = 0,
            name = "Adjectives",
        )

        val topicContentValues = ContentValues().apply {
            put("id", topic.id)
            put("name", topic.name)
        }

        db.insert(
            table = "topics",
            conflictAlgorithm = SQLiteDatabase.CONFLICT_REPLACE,
            values = topicContentValues,
        )

        val game = Game(
            id = null,
            topic = Topic(
                id = topic.id,
                name = topic.name,
                words = listOf(),
            ),
            startTimestamp = 5274,
            endTimestamp = 7303,
            rounds = listOf(),
        )

        val gameEntity = GameEntity.fromGame(game)
        gameDao.insert(gameEntity)

        val word = Faker().adjective.positive()
        val rounds = List(10) {
            Round(
                id = (it + 1).toLong(),
                game = game,
                word = Word(
                    id = (it + 1).toLong(),
                    name = word,
                ),
                number = it + 1,
                scrambledWord = word.scramble(),
                guess = null,
                isScored = false,
                isSkipped = true,
            )
        }.map { RoundEntity.fromRound(gameId = 0, round = it) }
        rounds.forEach { roundDao.insert(it) }

        val allGames = gameDao.getAllSince(startTimestamp = 0).firstOrNull() ?: emptyList()

        val expectedGame = GameWithTopicAndScoredRoundsCount(
            gameId = 0,
            topicName = topic.name,
            gameStartTimestamp = game.startTimestamp,
            gameEndTimestamp = game.endTimestamp!!,
            scoredRoundsCount = 0,
        )

        allGames.firstOrNull().shouldBeEqualTo(expectedGame)
    }
}
