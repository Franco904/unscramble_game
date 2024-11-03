package com.example.unscramble_game.core.data.local.database.entities.intermediates

import androidx.room.Embedded
import androidx.room.Relation
import com.example.unscramble_game.core.data.local.database.entities.TopicEntity
import com.example.unscramble_game.core.data.local.database.entities.WordEntity

data class TopicWithWords(
    @Embedded
    val topic: TopicEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "topic_id",
    )
    val words: List<WordEntity>,
)
