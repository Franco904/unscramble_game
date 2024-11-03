package com.example.unscramble_game.core.data.local.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "topics")
data class TopicEntity(
    @PrimaryKey
    @ColumnInfo("id")
    val id: Long,

    @ColumnInfo("name")
    val name: String,
)
