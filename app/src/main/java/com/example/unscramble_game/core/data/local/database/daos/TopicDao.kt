package com.example.unscramble_game.core.data.local.database.daos

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.example.unscramble_game.core.data.local.database.entities.intermediates.TopicWithWords

@Dao
interface TopicDao {
    @Transaction
    @Query("SELECT * FROM topics")
    suspend fun getAllWithWords(): List<TopicWithWords>
}
