package com.example.unscramble_game.core.data.local.daos

import androidx.room.Dao
import androidx.room.Query
import com.example.unscramble_game.core.data.local.entities.TopicEntity
import com.example.unscramble_game.core.data.local.entities.WordEntity

@Dao
interface TopicDao {
    @Query("""
        SELECT * FROM topics t
        INNER JOIN words w ON t.id = w.topic_id
    """)
    suspend fun getAllWithWords(): Map<TopicEntity, List<WordEntity>>
}
