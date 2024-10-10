package com.example.unscramble_game.core.domain.models

data class Topic(
    val name: String,
    val words: List<Word>,
)

//enum class GameTopic(val description: String) {
//    ADJECTIVES("Adjectives"),
//    ANIMAL_NAMES("Animal names"),
//    BASKETBALL_TEAMS("Basketball teams"),
//    COLOR_NAMES("Color names"),
//    HARRY_POTTER_SPELLS("Harry Potter spells"),
//    SPORT_NAMES("Sport names"),
//    MINECRAFT_MOBS("Minecraft mobs");
//}
