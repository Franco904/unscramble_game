package com.example.unscramble_game.core.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "rounds")
data class RoundEntity(
    @PrimaryKey
    @ColumnInfo("id")
    val id: Long,
    @ColumnInfo("game")
    val gameId: Long,
    @ColumnInfo("word")
    val wordId: Long,
    @ColumnInfo("scrambled_word")
    val scrambledWord: String,
    @ColumnInfo("guess")
    val guess: String? = null,
    @ColumnInfo("is_scored")
    val isScored: Boolean,
    @ColumnInfo("is_skipped")
    val isSkipped: Boolean,
)
