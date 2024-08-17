package com.example.unscramble_game

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.unscramble_game.core.presentation.theme.UnscrambleGameTheme
import com.example.unscramble_game.gamePanel.presentation.GamePanelScreen
import com.example.unscramble_game.previousGames.presentation.PreviousGamesScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            UnscrambleGameTheme {
                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = Routes.GamePanel,
                    builder = { routes(navController) },
                )
            }
        }
    }

    private fun NavGraphBuilder.routes(navController: NavController) {
        composable<Routes.GamePanel> {
            GamePanelScreen(
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
