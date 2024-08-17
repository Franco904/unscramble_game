package com.example.unscramble_game.previousGames.presentation

import android.util.Log
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.unscramble_game.core.presentation.theme.UnscrambleGameTheme
import com.example.unscramble_game.previousGames.presentation.models.PreviousGameUiState

@Composable
fun PreviousGamesScreen(
    modifier: Modifier = Modifier,
    onUpNavigation: () -> Boolean = { true },
) {
    Scaffold(
        topBar = {
            PreviousGamesTopBar { onUpNavigation() }
        },
        containerColor = MaterialTheme.colorScheme.background,
        modifier = modifier
    ) { innerPadding ->
        val topPadding = 12.dp + innerPadding.calculateTopPadding()
        val previousGamesGrouped = PreviousGamesDataSource.getPreviousGames()

        LazyColumn(
            contentPadding = PaddingValues(
                start = 16.dp,
                top = topPadding,
                end = 16.dp,
                bottom = 32.dp,
            ),
            modifier = Modifier
                .fillMaxSize()
        ) {
            previousGamesGrouped.forEach { (date, games) ->
                item {
                    Text(text = date, style = MaterialTheme.typography.bodyLarge)
                    Spacer(modifier = Modifier.height(8.dp))
                }
                items(games, key = { game -> game.topicText ?: "" }) { game ->
                    GameItem(game)
                    Spacer(modifier = Modifier.height(14.dp))
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PreviousGamesTopBar(
    modifier: Modifier = Modifier,
    onUpNavigation: () -> Boolean,
) {
    TopAppBar(
        title = {
            Text(
                "Previous games",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(start = 8.dp)
            )
        },
        navigationIcon = {
            IconButton(onClick = {
                onUpNavigation().takeIf { wasSucceeded -> !wasSucceeded }?.run {
                    Log.e("PreviousGamesScreen", "Navigation up failed!")
                }
            }) {
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
        modifier = modifier,
    )
}

@Composable
fun GameItem(
    game: PreviousGameUiState,
    modifier: Modifier = Modifier,
) {
    val interactionSource = remember { MutableInteractionSource() }

    val isCardPressed by interactionSource.collectIsPressedAsState()

    var elevation by remember { mutableStateOf(0.dp) }
    val elevationAnimated by animateDpAsState(
        targetValue = elevation,
        label = "elevationAnimated",
    )

    LaunchedEffect(isCardPressed) {
        elevation = if (isCardPressed) 2.dp else 0.dp
    }

    Surface(
        color = MaterialTheme.colorScheme.primaryContainer,
        shadowElevation = elevationAnimated,
        shape = MaterialTheme.shapes.small,
        interactionSource = interactionSource,
        onClick = {},
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 18.dp,
                    top = 16.dp,
                    end = 12.dp,
                )
        ) {
            Column(
                modifier = Modifier
                    .padding(bottom = 16.dp)
            ) {
                Text(
                    text = game.topicText!!,
                    style = MaterialTheme.typography.headlineSmall,
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = game.durationAndScoredRoundsText!!,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f),
                )
            }
            Text(
                text = game.totalScoreText!!,
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f),
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .align(Alignment.BottomEnd)
            )
        }
    }
}

@Preview
@Composable
fun PreviousGamesScreenPreview() {
    UnscrambleGameTheme {
        PreviousGamesScreen()
    }
}
