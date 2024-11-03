package com.example.unscramble_game.core.data.local.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.unscramble_game.core.data.local.database.entities.GameEntity
import com.example.unscramble_game.core.data.local.database.entities.intermediates.GameWithTopicAndScoredRoundsCount
import kotlinx.coroutines.flow.Flow

@Dao
interface GameDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(game: GameEntity): Long

    @Query(
        """
        SELECT t.name AS topicName, g.start_timestamp AS gameStartTimestamp, g.end_timestamp AS gameEndTimestamp,
        COUNT(r.is_scored) AS scoredRoundsCount FROM games g
        INNER JOIN topics t ON t.id = g.topic_id
        INNER JOIN rounds r ON g.id = r.game_id
    """
    )
    fun getAll(): Flow<List<GameWithTopicAndScoredRoundsCount>>
}
