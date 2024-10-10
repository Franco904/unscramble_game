package com.example.unscramble_game.core.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.unscramble_game.core.domain.models.Topic

@Entity(tableName = "topics")
data class TopicEntity(
    @PrimaryKey
    @ColumnInfo("id")
    val id: Long,

    @ColumnInfo("name")
    val name: String,
) {
    fun toTopic(words: List<WordEntity>) = Topic(
        name = name,
        words = words.map { it.toWord() },
    )
}
