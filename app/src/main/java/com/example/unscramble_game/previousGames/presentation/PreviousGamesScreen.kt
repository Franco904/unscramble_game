package com.example.unscramble_game.previousGames.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.unscramble_game.core.domain.models.Game
import kotlinx.collections.immutable.persistentListOf

@Composable
fun PreviousGamesScreen(
    modifier: Modifier = Modifier,
    onBackNavigation: () -> Unit = {},
) {
    Scaffold(
        topBar = {
            PreviousGamesTopBar(onBackNavigation)
        },
        containerColor = MaterialTheme.colorScheme.background,
        modifier = modifier
    ) { innerPadding ->
        val games = persistentListOf<Game>(

        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    start = 24.dp,
                    top = innerPadding.calculateTopPadding(),
                    end = 24.dp,
                    bottom = 32.dp,
                )
                .background(MaterialTheme.colorScheme.primaryContainer)
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            Text(text = "17/7/2024")
            Spacer(modifier = Modifier.height(24.dp))
            Card(
                modifier = Modifier.fillMaxSize()
            ) {

            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PreviousGamesTopBar(
    onBackNavigation: () -> Unit = {},
) {
    TopAppBar(
        title = { Text("") },
        navigationIcon = {
            IconButton(onClick = onBackNavigation) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
        ),
    )
}

@Preview
@Composable
fun PreviousGamesScreenPreview() {
    PreviousGamesScreen()
}
