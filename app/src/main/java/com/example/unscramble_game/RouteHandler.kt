package com.example.unscramble_game

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.unscramble_game.core.presentation.utils.alsoInvoke
import com.example.unscramble_game.gamePanel.presentation.GamePanelScreen
import com.example.unscramble_game.gamePanel.presentation.GamePanelViewModel
import com.example.unscramble_game.previousGames.presentation.PreviousGamesScreen

object RouteHandler {
    fun NavGraphBuilder.routes(navController: NavController) {
        composable<Routes.GamePanel> {
            GamePanelScreen(
                viewModel = viewModel<GamePanelViewModel>().alsoInvoke { init() },
                onNavigateToGameHistory = {
                    navController.navigate(Routes.PreviousGames)
                },
            )
        }
        composable<Routes.PreviousGames> {
            PreviousGamesScreen(
                onUpNavigation = navController::navigateUp,
            )
        }
    }
}
