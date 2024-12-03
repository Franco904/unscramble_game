package com.example.unscramble_game.gamePanel.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.History
import androidx.compose.material3.Badge
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.unscramble_game.R
import com.example.unscramble_game.core.presentation.theme.AppTheme
import com.example.unscramble_game.core.presentation.theme.spanTypography
import com.example.unscramble_game.core.presentation.utils.showTextShareSheet
import com.example.unscramble_game.core.presentation.utils.style
import com.example.unscramble_game.core.presentation.utils.textSpan
import com.example.unscramble_game.gamePanel.presentation.composables.GameFinishedScoreDialog
import com.example.unscramble_game.gamePanel.presentation.composables.GameTopicSelectionDialog
import com.example.unscramble_game.gamePanel.presentation.models.GameStatus
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList

@Composable
fun GamePanelScreen(
    modifier: Modifier = Modifier,
    viewModel: GamePanelViewModel = hiltViewModel(),
    onNavigateToGameHistory: () -> Unit = {},
) {
    val gameControlUiState by viewModel.gameControlUiState.collectAsStateWithLifecycle()
    val gameFormUiState by viewModel.gameFormUiState.collectAsStateWithLifecycle()
    val topics by viewModel.topics.collectAsStateWithLifecycle()

    val isGameNotStartedState =
        gameControlUiState.gameStatus in listOf(GameStatus.NotStarted, GameStatus.TopicSelection)

    val focusManager = LocalFocusManager.current

    Scaffold(
        topBar = {
            if (isGameNotStartedState) GamePanelTopBar(onNavigateToGameHistory)
        },
        containerColor = MaterialTheme.colorScheme.background,
        modifier = modifier
    ) { innerPadding ->
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures { focusManager.clearFocus() }
                }
                .padding(
                    horizontal = 24.dp + innerPadding.calculateLeftPadding(LayoutDirection.Ltr),
                    vertical = 32.dp + innerPadding.calculateTopPadding(),
                )
                .verticalScroll(rememberScrollState())
        ) {
            val isStartedOrFinishedState =
                gameControlUiState.gameStatus in persistentListOf(
                    GameStatus.Started,
                    GameStatus.Finished
                )

            if (isStartedOrFinishedState) {
                Row {
                    GameTotalScoreSection(totalScore = gameControlUiState.totalScore.toString())
                    Spacer(modifier = Modifier.weight(1f))
                    GameTopicSection(
                        topic = gameControlUiState.topicName ?: "",
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
            GameMainPanel {
                val isNotStartedOrTopicSelectionState =
                    gameControlUiState.gameStatus in persistentListOf(
                        GameStatus.NotStarted,
                        GameStatus.TopicSelection
                    )

                if (isNotStartedOrTopicSelectionState) GameNotStartedPanel()
                else GameStartedPanel(
                    round = gameControlUiState.round.toString(),
                    scrambledRoundWord = gameControlUiState.scrambledRoundWord,
                    guess = gameFormUiState.guess.value,
                    guessError = gameFormUiState.guessError,
                    onGuessChanged = viewModel::onGuessTextChanged,
                    onGuessInputDone = { focusManager.clearFocus() },
                    onFocusChanged = viewModel::onGuessFieldFocusChanged,
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
            GamePrimaryButton(
                buttonText = stringResource(
                    id = gameControlUiState.primaryButtonText
                        ?: R.string.unscramble_game_start_game_primary_btn
                ),
                onClick = viewModel::onPrimaryButtonClicked,
            )
            AnimatedVisibility(isStartedOrFinishedState) {
                GameSecondaryButton(
                    buttonText = stringResource(
                        id = gameControlUiState.secondaryButtonText
                            ?: R.string.unscramble_game_no_secondary_btn
                    ),
                    onClick = viewModel::onSecondaryButtonClicked,
                )
            }
            if (gameControlUiState.gameStatus == GameStatus.TopicSelection) {
                GameTopicSelectionDialog(
                    topics = topics.map { it.name ?: "" }.toPersistentList(),
                    onCancel = viewModel::onCancelTopicSelection,
                    onStart = viewModel::onTopicSelected,
                )
            } else if (gameControlUiState.gameStatus == GameStatus.Finished) {
                val context = LocalContext.current

                GameFinishedScoreDialog(
                    totalScore = gameControlUiState.totalScore,
                    onShareGame = {
                        context.showTextShareSheet(
                            text = context.getString(
                                R.string.unscramble_game_sharesheet_share_your_game_with_text,
                                gameControlUiState.totalScore.toString(),
                            ),
                        )
                    },
                    onRestartGame = viewModel::restartGame,
                    onQuitGame = viewModel::quitGame,
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun GamePanelTopBar(
    onNavigateToGameHistory: () -> Unit,
) {
    TopAppBar(
        title = { Text("") },
        actions = {
            GamePanelActionToGameHistory(onNavigateToGameHistory)
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
        )
    )
}

@Composable
private fun GamePanelActionToGameHistory(
    onNavigateToGameHistory: () -> Unit,
) {
    IconButton(onClick = onNavigateToGameHistory) {
        Icon(
            Icons.Outlined.History,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
        )
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
            containerColor = MaterialTheme.colorScheme.primaryContainer,
        ),
        shape = MaterialTheme.shapes.medium,
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
                    style = MaterialTheme.spanTypography.headlineMedium,
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
    Button(
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
fun GamePanelScreenPreview() {
    AppTheme {
        GamePanelScreen()
    }
}
