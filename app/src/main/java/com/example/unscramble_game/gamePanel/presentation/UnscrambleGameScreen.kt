package com.example.unscramble_game.gamePanel.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActionScope
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Badge
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.unscramble_game.R
import com.example.unscramble_game.core.presentation.theme.UnscrambleGameTheme
import com.example.unscramble_game.core.presentation.theme.spanTypography
import com.example.unscramble_game.core.presentation.utils.style
import com.example.unscramble_game.core.presentation.utils.textSpan
import com.example.unscramble_game.gamePanel.domain.models.GameTopic
import com.example.unscramble_game.gamePanel.presentation.composables.GameFinishedScoreDialog
import com.example.unscramble_game.gamePanel.presentation.composables.GameTopicSelectionDialog
import com.example.unscramble_game.gamePanel.presentation.models.GameState
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList

@Composable
fun UnscrambleGameScreen(
    modifier: Modifier = Modifier,
    viewModel: UnscrambleGameViewModel = viewModel<UnscrambleGameViewModel>().apply { init() },
) {
    val gameControlState by viewModel.gameControlState.collectAsStateWithLifecycle()
    val gameFormState = viewModel.gameFormState

    val focusManager = LocalFocusManager.current

    Column(
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .pointerInput(Unit) {
                detectTapGestures(onTap = { focusManager.clearFocus() })
            }
            .padding(horizontal = 24.dp, vertical = 32.dp)
            .verticalScroll(rememberScrollState())
    ) {
        val isStartedOrFinishedState =
            gameControlState.gameState in persistentListOf(GameState.STARTED, GameState.FINISHED)

        if (isStartedOrFinishedState) {
            Row {
                GameTotalScoreSection(totalScore = gameControlState.totalScore.toString())
                Spacer(modifier = Modifier.weight(1f))
                GameTopicSection(
                    topic = gameControlState.topicWords?.topic?.description!!,
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
        GameMainPanel {
            val isNotStartedOrTopicSelectionState = gameControlState.gameState in persistentListOf(
                GameState.NOT_STARTED,
                GameState.TOPIC_SELECTION
            )

            if (isNotStartedOrTopicSelectionState) GameNotStartedPanel()
            else GameStartedPanel(
                round = gameControlState.round.toString(),
                scrambledRoundWord = gameControlState.scrambledRoundWord,
                guess = gameFormState.guess.text,
                guessError = gameFormState.guessError,
                onGuessChanged = viewModel::onGuessTextChanged,
                onGuessInputDone = { focusManager.clearFocus() },
                onFocusChanged = viewModel::onGuessFieldFocusChanged,
            )
        }
        Spacer(modifier = Modifier.height(32.dp))
        GamePrimaryButton(
            buttonText = stringResource(
                id = gameControlState.primaryButtonText
                    ?: R.string.unscramble_game_start_game_primary_btn
            ),
            onClick = viewModel::onPrimaryButtonClicked,
        )
        AnimatedVisibility(isStartedOrFinishedState) {
            GameSecondaryButton(
                buttonText = stringResource(
                    id = gameControlState.secondaryButtonText
                        ?: R.string.unscramble_game_no_secondary_btn
                ),
                onClick = viewModel::onSecondaryButtonClicked,
            )
        }
        if (gameControlState.gameState == GameState.TOPIC_SELECTION) {
            GameTopicSelectionDialog(
                topics = GameTopic.entries.map { it.description }.toPersistentList(),
                onCancel = viewModel::onCancelTopicSelection,
                onStart = viewModel::onTopicSelected,
            )
        } else if (gameControlState.gameState == GameState.FINISHED) {
            GameFinishedScoreDialog(
                totalScore = gameControlState.totalScore,
                onRestartGame = viewModel::restartGame,
                onQuitGame = viewModel::quitGame,
            )
        }
    }
}

@Composable
private fun GameTotalScoreSection(
    totalScore: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = buildAnnotatedString {
            textSpan(stringResource(R.string.unscramble_game_score))
            textSpan(
                totalScore.style(
                    SpanStyle(
                        fontWeight = FontWeight.Medium,
                        letterSpacing = 0.5.sp,
                    )
                )
            )
        },
        modifier = modifier
    )
}

@Composable
private fun GameTopicSection(
    topic: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = buildAnnotatedString {
            textSpan(stringResource(R.string.unscramble_game_topic))
            textSpan(
                topic.style(
                    SpanStyle(
                        fontWeight = FontWeight.Medium,
                        letterSpacing = 0.5.sp,
                    )
                )
            )
        },
        modifier = modifier
    )
}

@Composable
private fun GameMainPanel(
    modifier: Modifier = Modifier,
    mainPanel: @Composable () -> Unit,
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
        ),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp,
        ),
        modifier = modifier
            .fillMaxWidth()
    ) {
        mainPanel()
    }
}

@Composable
private fun GameNotStartedPanel(
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxWidth()
            .padding(32.dp)
    ) {
        Text(
            text = buildAnnotatedString {
                textSpan(
                    stringResource(R.string.app_owner_text),
                    style = MaterialTheme.spanTypography.bodyLarge,
                )
                textSpan(
                    stringResource(R.string.app_name),
                    style = MaterialTheme.spanTypography.headlineMedium
                )
            },
        )
    }
}

@Composable
private fun GameStartedPanel(
    round: String,
    scrambledRoundWord: String?,
    guess: String?,
    guessError: Int?,
    onGuessChanged: (String) -> Unit,
    onGuessInputDone: KeyboardActionScope.() -> Unit,
    onFocusChanged: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    var isStartedStateInitialCompositionCompleted by remember { mutableStateOf(false) }

    // Side effects are always executed last (after the composition)
    LaunchedEffect(Unit) {
        isStartedStateInitialCompositionCompleted = true
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Badge(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier
                .clip(MaterialTheme.shapes.extraSmall)
                .align(Alignment.End)
        ) {
            Text(
                text = stringResource(R.string.unscramble_game_current_round, round),
                modifier = Modifier.padding(2.dp),
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = scrambledRoundWord ?: "",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier
                .align(Alignment.CenterHorizontally),
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(R.string.unscramble_guess_the_scrambled_word),
            modifier = Modifier
                .align(Alignment.CenterHorizontally),
        )
        Spacer(modifier = Modifier.height(16.dp))
        GameGuessTextField(
            guess = guess,
            guessError = guessError,
            onGuessChanged = onGuessChanged,
            onGuessInputDone = onGuessInputDone,
            onFocusChanged = onFocusChanged,
            isStartedStateInitialCompositionCompleted = isStartedStateInitialCompositionCompleted,
        )
    }
}

@Composable
private fun GameGuessTextField(
    guess: String?,
    guessError: Int?,
    onGuessChanged: (String) -> Unit,
    onGuessInputDone: KeyboardActionScope.() -> Unit,
    onFocusChanged: (Boolean) -> Unit,
    isStartedStateInitialCompositionCompleted: Boolean,
    modifier: Modifier = Modifier,
) {
    val isGuessInvalid = guessError != null

    OutlinedTextField(
        value = guess ?: "",
        onValueChange = onGuessChanged,
        keyboardActions = KeyboardActions(
            onDone = onGuessInputDone,
        ),
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Done,
        ),
        isError = isGuessInvalid,
        placeholder = { Text(text = stringResource(R.string.unscramble_your_guess)) },
        colors = TextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.background,
            unfocusedContainerColor = MaterialTheme.colorScheme.background,
            errorContainerColor = MaterialTheme.colorScheme.background,
            errorCursorColor = MaterialTheme.colorScheme.error,
        ),
        supportingText = {
            AnimatedVisibility(isGuessInvalid) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = if (isGuessInvalid) stringResource(guessError!!) else "",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                )
            }
        },
        shape = MaterialTheme.shapes.medium,
        singleLine = true,
        modifier = modifier
            .onFocusChanged {
                if (isStartedStateInitialCompositionCompleted) {
                    onFocusChanged(it.isFocused)
                }
            }
    )
}

@Composable
private fun GamePrimaryButton(
    buttonText: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ElevatedButton(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
        ),
        shape = MaterialTheme.shapes.medium,
        modifier = modifier
            .fillMaxWidth()
    ) {
        Text(text = buttonText)
    }
}

@Composable
private fun GameSecondaryButton(
    buttonText: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    OutlinedButton(
        onClick = onClick,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
        shape = MaterialTheme.shapes.medium,
        modifier = modifier
            .fillMaxWidth()
    ) {
        Text(text = buttonText)
    }
}

@Preview
@Composable
fun UnscrambleGameUiPreview() {
    UnscrambleGameTheme {
        UnscrambleGameScreen()
    }
}