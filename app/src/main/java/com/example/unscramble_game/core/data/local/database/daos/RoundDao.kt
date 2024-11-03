package com.example.unscramble_game.core.data.local.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.example.unscramble_game.core.data.local.database.entities.RoundEntity

@Dao
interface RoundDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(round: RoundEntity)
}
