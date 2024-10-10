package com.example.unscramble_game.core.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "games")
data class GameEntity(
    @PrimaryKey
    @ColumnInfo("id")
    val id: Long,
    @ColumnInfo("topic_id")
    val topicId: Long,
    @ColumnInfo("start_date")
    val startDate: Date,
    @ColumnInfo("end_date")
    val endDate: Date,
)
