package com.example.unscramble_game.core.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.unscramble_game.core.data.local.converters.DateTypeConverter
import com.example.unscramble_game.core.data.local.daos.TopicDao
import com.example.unscramble_game.core.data.local.entities.GameEntity
import com.example.unscramble_game.core.data.local.entities.RoundEntity
import com.example.unscramble_game.core.data.local.entities.TopicEntity
import com.example.unscramble_game.core.data.local.entities.WordEntity

@Database(
    entities = [
        GameEntity::class,
        RoundEntity::class,
        TopicEntity::class,
        WordEntity::class,
    ],
    version = UnscrambleGameDatabase.DB_VERSION,
)
@TypeConverters(
    value = [
        DateTypeConverter::class,
    ]
)
abstract class UnscrambleGameDatabase : RoomDatabase() {
    abstract fun topicDao(): TopicDao

    companion object {
        const val DB_VERSION = 1
        const val DB_NAME = "unscramble_game_db"
    }
}
