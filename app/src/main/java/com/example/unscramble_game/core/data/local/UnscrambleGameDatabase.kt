package com.example.unscramble_game.core.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.unscramble_game.core.data.local.converters.DateTypeConverter
import com.example.unscramble_game.core.data.local.daos.TopicDao
import com.example.unscramble_game.core.data.local.entities.TopicEntity

@Database(
    entities = [
        TopicEntity::class,
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
