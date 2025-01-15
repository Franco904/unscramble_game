package com.example.unscramble_game.core.di

import com.example.unscramble_game.gamePanel.presentation.GamePanelViewModel
import com.example.unscramble_game.previousGames.presentation.PreviousGamesViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        GamePanelViewModel(
            repository = get(),
        )
    }

    viewModel {
        PreviousGamesViewModel(
            repository = get(),
        )
    }
}
