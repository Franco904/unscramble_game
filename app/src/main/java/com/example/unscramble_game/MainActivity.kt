package com.example.unscramble_game

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.unscramble_game.core.presentation.theme.UnscrambleGameTheme
import com.example.unscramble_game.gamePanel.presentation.GamePanelScreen
import kotlinx.serialization.Serializable

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            UnscrambleGameTheme {
                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = Routes.GamePanel,
                    enterTransition = { fadeIn(animationSpec = tween(150)) },
                    exitTransition = { fadeOut(animationSpec = tween(150)) },
                ) {
                    composable<Routes.GamePanel> {
                        GamePanelScreen(
                            onNavigateToGameHistory = {
                                navController.navigate(Routes.GameHistory)
                            }
                        )
                    }
                    composable<Routes.GameHistory> {
                        // TODO: Implement GameHistoryScreen
                    }
                }
            }
        }
    }
}

sealed interface Routes {
    @Serializable
    object GamePanel

    @Serializable
    object GameHistory
}