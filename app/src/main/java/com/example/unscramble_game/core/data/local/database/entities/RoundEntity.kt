package com.example.unscramble_game.core.data.local.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.unscramble_game.core.domain.models.Round

@Entity(tableName = "rounds")
data class RoundEntity(
    @PrimaryKey
    @ColumnInfo("id")
    val id: Long? = null,

    @ColumnInfo("game_id")
    val gameId: Long,

    @ColumnInfo("word_id")
    val wordId: Long,

    @ColumnInfo("scrambled_word")
    val scrambledWord: String,

    @ColumnInfo("guess")
    val guess: String? = null,

    @ColumnInfo("is_scored")
    val isScored: Boolean,

    @ColumnInfo("is_skipped")
    val isSkipped: Boolean,
) {
    companion object {
        fun fromRound(gameId: Long, round: Round) = RoundEntity(
            id = round.id,
            gameId = gameId,
            wordId = round.word.id,
            scrambledWord = round.scrambledWord,
            guess = round.guess,
            isScored = round.isScored,
            isSkipped = round.isSkipped,
        )
    }
}
