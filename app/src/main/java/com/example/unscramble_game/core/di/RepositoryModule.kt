package com.example.unscramble_game.core.di

import com.example.unscramble_game.gamePanel.data.GamePanelRepository
import com.example.unscramble_game.previousGames.data.PreviousGamesRepository
import org.koin.dsl.module

val repositoryModule = module {
    single {
        GamePanelRepository(
            gameDao = get(),
            roundDao = get(),
            topicDao = get(),
        )
    }

    single {
        PreviousGamesRepository(
            gameDao = get(),
        )
    }
}
