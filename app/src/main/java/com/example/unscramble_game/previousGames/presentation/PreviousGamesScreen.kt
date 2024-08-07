package com.example.unscramble_game.previousGames.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.unscramble_game.core.domain.models.GameTopic
import com.example.unscramble_game.core.presentation.theme.UnscrambleGameTheme
import com.example.unscramble_game.previousGames.presentation.models.PreviousGameState
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentMapOf

@Composable
fun PreviousGamesScreen(
    modifier: Modifier = Modifier,
    onBackNavigation: () -> Unit = {},
) {
    Scaffold(
        topBar = {
            PreviousGamesTopBar { onBackNavigation() }
        },
        containerColor = MaterialTheme.colorScheme.background,
        modifier = modifier
    ) { innerPadding ->
        val previousGamesGrouped = getPreviousGames()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(
                    start = 16.dp,
                    top = 12.dp + innerPadding.calculateTopPadding(),
                    end = 16.dp,
                    bottom = 32.dp,
                )
        ) {
            for (dayAndPreviousGames in previousGamesGrouped.entries) {
                val (day, games) = dayAndPreviousGames

                Text(
                    text = day,
                    style = MaterialTheme.typography.bodyLarge,
                    letterSpacing = 0.5.sp,
                )
                Spacer(modifier = Modifier.height(8.dp))
                for (game in games) {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                        ),
                        shape = MaterialTheme.shapes.small,
                        onClick = {},
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
                    if (game != games.last()) {
                        Spacer(modifier = Modifier.height(14.dp))
                    }
                }
                if (dayAndPreviousGames != previousGamesGrouped.entries.last()) {
                    Spacer(modifier = Modifier.height(18.dp))
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PreviousGamesTopBar(
    modifier: Modifier = Modifier,
    onBackNavigation: () -> Unit = {},
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
        modifier = modifier,
    )
}

private fun getPreviousGames() = persistentMapOf(
    "Today" to persistentListOf(
        PreviousGameState(
            topicText = GameTopic.MINECRAFT_MOBS.description,
            durationAndScoredRoundsText = "5m58s • Scored 8/10",
            totalScoreText = "80",
        ),
        PreviousGameState(
            topicText = GameTopic.BASKETBALL_TEAMS.description,
            durationAndScoredRoundsText = "1m5s • Scored 10/10",
            totalScoreText = "100",
        ),
    ),
    "Yesterday" to persistentListOf(
        PreviousGameState(
            topicText = GameTopic.COLOR_NAMES.description,
            durationAndScoredRoundsText = "9m34s • Scored 2/10",
            totalScoreText = "20",
        ),
    ),
    "July 15" to persistentListOf(
        PreviousGameState(
            topicText = GameTopic.HARRY_POTTER_SPELLS.description,
            durationAndScoredRoundsText = "46s • Scored 9/10",
            totalScoreText = "90",
        ),
        PreviousGameState(
            topicText = GameTopic.SPORT_NAMES.description,
            durationAndScoredRoundsText = "3m24s • Scored 4/10",
            totalScoreText = "40",
        ),
    ),
)

@Preview
@Composable
fun PreviousGamesScreenPreview() {
    UnscrambleGameTheme {
        PreviousGamesScreen()
    }
}
