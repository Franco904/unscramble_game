package com.example.unscramble_game.core.data.local.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.unscramble_game.core.domain.models.Game

@Entity(tableName = "games")
data class GameEntity(
    @PrimaryKey
    @ColumnInfo("id")
    val id: Long? = null,

    @ColumnInfo("topic_id")
    val topicId: Long,

    @ColumnInfo("start_timestamp")
    val startTimestamp: Long,

    @ColumnInfo("end_timestamp")
    val endTimestamp: Long,
) {
    companion object {
        fun fromGame(game: Game) = GameEntity(
            id = game.id,
            topicId = game.topic.id,
            startTimestamp = game.startTimestamp,
            endTimestamp = game.endTimestamp!!,
        )
    }
}
