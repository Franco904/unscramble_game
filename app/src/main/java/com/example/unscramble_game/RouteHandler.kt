package com.example.unscramble_game

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.unscramble_game.core.presentation.utils.invokeAfterComposition
import com.example.unscramble_game.gamePanel.presentation.GamePanelScreen
import com.example.unscramble_game.gamePanel.presentation.GamePanelViewModel
import com.example.unscramble_game.previousGames.presentation.PreviousGamesScreen
import com.example.unscramble_game.previousGames.presentation.PreviousGamesViewModel
import org.koin.androidx.compose.koinViewModel

object RouteHandler {
    fun NavGraphBuilder.routes(navController: NavController) {
        composable<Routes.GamePanel> {
            GamePanelScreen(
                viewModel = koinViewModel<GamePanelViewModel>()
                    .invokeAfterComposition { init() },
                onNavigateToGameHistory = {
                    navController.navigate(Routes.PreviousGames)
                },
            )
        }

        composable<Routes.PreviousGames> {
            PreviousGamesScreen(
                viewModel = koinViewModel<PreviousGamesViewModel>()
                    .invokeAfterComposition { init() },
                onUpNavigation = navController::navigateUp,
            )
        }
    }
}
