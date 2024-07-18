package com.example.unscramble_game.gamePanel.presentation.utils

import com.example.unscramble_game.core.domain.models.GameTopic
import com.example.unscramble_game.core.miscellaneous.faker
import com.example.unscramble_game.core.presentation.utils.toTitleCase

typealias WordBuilder = () -> String

object GameTopicWordsBuilder {
    private val topicToWordBuilder = mapOf<GameTopic, WordBuilder>(
        GameTopic.ADJECTIVES to faker.adjective::positive,
        GameTopic.ANIMAL_NAMES to faker.animal::name,
        GameTopic.BASKETBALL_TEAMS to faker.basketball::teams,
        GameTopic.COLOR_NAMES to faker.color::name,
        GameTopic.HARRY_POTTER_SPELLS to faker.harryPotter::spells,
        GameTopic.SPORT_NAMES to faker.sport::summerOlympics,
        GameTopic.MINECRAFT_MOBS to faker.minecraft::mobs,
    )

    fun GameTopic.getWords(): List<String> {
        val wordBuilder = topicToWordBuilder[this@getWords]
            ?: throw Exception("No word builder for game topic $this.")

        return List(10) { wordBuilder() }
            .toSet()
            .toMutableList()
            .apply {
                while (size != 10) {
                    wordBuilder().let { if (it !in this) add(it) }
                }
            }
            .map { it.toTitleCase() }
            .toList()
    }
}
