package com.example.unscramble_game

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.unscramble_game.RouteHandler.routes
import com.example.unscramble_game.core.presentation.theme.UnscrambleGameTheme

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
}
