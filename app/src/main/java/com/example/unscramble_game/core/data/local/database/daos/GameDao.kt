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
        SELECT g.id AS gameId, t.name AS topicName, g.start_timestamp AS gameStartTimestamp, g.end_timestamp AS gameEndTimestamp,
        COUNT(CASE WHEN r.is_scored = 1 THEN 1 ELSE NULL END) AS scoredRoundsCount FROM games g
        INNER JOIN topics t ON t.id = g.topic_id
        INNER JOIN rounds r ON g.id = r.game_id
        WHERE g.start_timestamp >= :startTimestamp
        GROUP BY g.id
    """
    )
    fun getAllSince(startTimestamp: Long): Flow<List<GameWithTopicAndScoredRoundsCount>>
}
