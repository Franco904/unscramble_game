package com.example.unscramble_game.gamePanel.presentation.composables

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.unscramble_game.R
import com.example.unscramble_game.core.presentation.composables.DropdownField
import com.example.unscramble_game.core.presentation.theme.AppTheme
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
fun GameTopicSelectionDialog(
    topics: ImmutableList<String>,
    modifier: Modifier = Modifier,
    onStart: (String?) -> Unit = {},
    onCancel: () -> Unit = {},
) {
    var selectedTopic by rememberSaveable { mutableStateOf<String?>(null) }

    BackHandler { onCancel() }

    AlertDialog(
        title = {
            Text(text = stringResource(R.string.unscramble_game_select_game_topic))
        },
        text = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                DropdownField(
                    placeholderText = stringResource(R.string.unscramble_game_label_topic),
                    items = topics,
                    onItemSelected = { item -> selectedTopic = item }
                )
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = stringResource(R.string.unscramble_game_topic_selection_explanation),
                    style = MaterialTheme.typography.bodyLarge,
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onCancel) {
                Text(
                    text = stringResource(id = R.string.unscramble_game_dialog_button_cancel),
                    style = MaterialTheme.typography.titleMedium,
                )
            }
        },
        confirmButton = {
            TextButton(
                enabled = selectedTopic != null,
                onClick = { onStart(selectedTopic) },
            ) {
                Text(
                    text = stringResource(id = R.string.unscramble_game_dialog_button_start),
                    style = MaterialTheme.typography.titleMedium,
                )
            }
        },
        onDismissRequest = {},
        shape = MaterialTheme.shapes.small,
        modifier = modifier.width(450.dp)
    )
}

@Preview
@Composable
fun GameTopicSelectionDialogPreview() {
    AppTheme {
        GameTopicSelectionDialog(topics = persistentListOf())
    }
}
