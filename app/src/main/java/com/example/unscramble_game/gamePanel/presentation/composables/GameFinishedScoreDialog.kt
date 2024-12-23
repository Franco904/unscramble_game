package com.example.unscramble_game.gamePanel.presentation.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.unscramble_game.R
import com.example.unscramble_game.core.presentation.theme.AppTheme

@Composable
fun GameFinishedScoreDialog(
    totalScore: Int,
    modifier: Modifier = Modifier,
    onShareGame: () -> Unit = {},
    onRestartGame: () -> Unit = {},
    onQuitGame: () -> Unit = {},
) {
    AlertDialog(
        title = {
            Text(
                text = stringResource(if (totalScore >= 80) R.string.unscramble_game_congratulations else R.string.unscramble_game_game_over),
                style = MaterialTheme.typography.headlineSmall,
            )
        },
        text = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Text(
                    text = stringResource(R.string.unscramble_your_final_score),
                    style = MaterialTheme.typography.bodyLarge,
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = totalScore.toString(),
                    style = MaterialTheme.typography.headlineMedium,
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(MaterialTheme.shapes.small)
                        .clickable { onShareGame() }
                        .padding(8.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_share_24),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = stringResource(R.string.unscramble_game_dialog_button_share_your_game),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Medium,
                    )
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onRestartGame) {
                Text(
                    text = stringResource(R.string.unscramble_game_dialog_button_restart),
                    style = MaterialTheme.typography.titleMedium,
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onQuitGame) {
                Text(
                    text = stringResource(R.string.unscramble_game_dialog_button_quit),
                    style = MaterialTheme.typography.titleMedium,
                )
            }
        },
        onDismissRequest = {},
        shape = MaterialTheme.shapes.small,
        modifier = modifier.width(300.dp)
    )
}

@Preview
@Composable
fun GameFinishedScoreDialogPreview() {
    AppTheme {
        GameFinishedScoreDialog(totalScore = 100)
    }
}