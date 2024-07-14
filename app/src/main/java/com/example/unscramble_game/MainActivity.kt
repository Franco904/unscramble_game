package com.example.unscramble_game

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.unscramble_game.core.presentation.theme.UnscrambleGameTheme
import com.example.unscramble_game.gamePanel.presentation.UnscrambleGameScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            UnscrambleGameTheme {
                UnscrambleGameScreen()
            }
        }
    }
}