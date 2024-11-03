package com.example.unscramble_game.core.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.unscramble_game.core.data.local.database.converters.DateTypeConverter
import com.example.unscramble_game.core.data.local.database.daos.GameDao
import com.example.unscramble_game.core.data.local.database.daos.RoundDao
import com.example.unscramble_game.core.data.local.database.daos.TopicDao
import com.example.unscramble_game.core.data.local.database.entities.GameEntity
import com.example.unscramble_game.core.data.local.database.entities.RoundEntity
import com.example.unscramble_game.core.data.local.database.entities.TopicEntity
import com.example.unscramble_game.core.data.local.database.entities.WordEntity
import com.example.unscramble_game.core.data.utils.migrate

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
    abstract fun gameDao(): GameDao

    abstract fun roundDao(): RoundDao

    abstract fun topicDao(): TopicDao

    companion object {
        const val DB_VERSION = 2
        private const val DB_NAME = "unscramble_game_db"

        fun buildDatabase(appContext: Context): UnscrambleGameDatabase {
            return Room.databaseBuilder(
                context = appContext,
                klass = UnscrambleGameDatabase::class.java,
                name = DB_NAME,
            )
                .addCallback(object : Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        appContext.migrate(db, endVersion = 1)
                    }
                })
                .build()
        }
    }
}
