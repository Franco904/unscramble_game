package com.example.unscramble_game.core.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.unscramble_game.core.domain.models.Word

@Entity(tableName = "words")
data class WordEntity(
    @PrimaryKey
    @ColumnInfo("id")
    val id: Long,
    @ColumnInfo("topic_id")
    val topicId: Long,
    @ColumnInfo("name")
    val name: String,
) {
    fun toWord() = Word(name = name)
}
