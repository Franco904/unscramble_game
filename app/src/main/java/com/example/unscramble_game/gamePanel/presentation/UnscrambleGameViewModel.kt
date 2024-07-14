package com.example.unscramble_game.gamePanel.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.unscramble_game.R
import com.example.unscramble_game.core.domain.models.Guess
import com.example.unscramble_game.core.miscellaneous.faker
import com.example.unscramble_game.core.presentation.utils.getStoredStateFlow
import com.example.unscramble_game.core.presentation.utils.scramble
import com.example.unscramble_game.core.presentation.utils.updateStateFlow
import com.example.unscramble_game.core.presentation.validation.ValidationMessageConverter.toPresentationMessage
import com.example.unscramble_game.gamePanel.domain.models.GameTopic
import com.example.unscramble_game.gamePanel.presentation.models.GameControlState
import com.example.unscramble_game.gamePanel.presentation.models.GameFormState
import com.example.unscramble_game.gamePanel.presentation.models.GameState
import com.example.unscramble_game.gamePanel.presentation.models.GameTopicWords
import com.example.unscramble_game.gamePanel.presentation.utils.GameTopicWordsBuilder.getWords
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.launch

class UnscrambleGameViewModel(
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {
    val gameControlState =
        savedStateHandle.getStoredStateFlow(GAME_CONTROL_STATE_KEY, GameControlState())

    var gameFormState by mutableStateOf(GameFormState())
        private set

    private lateinit var topicToWords: Map<GameTopic, ImmutableList<String>>

    fun init() {
        viewModelScope.launch {
            topicToWords = mapOf(
                GameTopic.ADJECTIVES to GameTopic.ADJECTIVES.getWords(),
                GameTopic.ANIMAL_NAMES to GameTopic.ANIMAL_NAMES.getWords(),
                GameTopic.BASKETBALL_TEAMS to GameTopic.BASKETBALL_TEAMS.getWords(),
                GameTopic.COLOR_NAMES to GameTopic.COLOR_NAMES.getWords(),
                GameTopic.HARRY_POTTER_SPELLS to GameTopic.HARRY_POTTER_SPELLS.getWords(),
                GameTopic.SPORT_NAMES to GameTopic.SPORT_NAMES.getWords(),
                GameTopic.MINECRAFT_MOBS to GameTopic.MINECRAFT_MOBS.getWords(),
            )
        }
    }

    fun onGuessTextChanged(newGuessText: String) {
        gameFormState = gameFormState.copy(guess = Guess(text = newGuessText))
    }

    fun onGuessFieldFocusChanged(isFocused: Boolean) {
        if (isFocused) {
            gameFormState = gameFormState.copy(guessError = null)
            return
        }

        val guessValidationResult = gameFormState.guess.validate()

        if (!guessValidationResult.isSuccessful) {
            gameFormState = gameFormState.copy(
                guessError = guessValidationResult.error.toPresentationMessage(),
            )
        }
    }

    fun onPrimaryButtonClicked() {
        when (gameControlState.value.gameState) {
            GameState.NOT_STARTED -> initTopicSelection()
            GameState.TOPIC_SELECTION -> {}
            GameState.STARTED -> submitGuess()
            GameState.FINISHED -> restartGame()
        }
    }

    private fun initTopicSelection() {
        savedStateHandle.updateStateFlow(GAME_CONTROL_STATE_KEY, gameControlState) { currentUiState ->
            currentUiState.copy(
                gameState = GameState.TOPIC_SELECTION,
                topicWords = null,
            )
        }
    }

    fun onCancelTopicSelection() {
        savedStateHandle.updateStateFlow(GAME_CONTROL_STATE_KEY, gameControlState) { currentUiState ->
            currentUiState.copy(gameState = GameState.NOT_STARTED)
        }
    }

    fun onTopicSelected(selectedTopicName: String?) {
        val topicWords =
            topicToWords.entries.find { it.key.description == selectedTopicName }

        if (topicWords == null) {
            onCancelTopicSelection()
            return
        }

        startGame(GameTopicWords(topicWords.key, topicWords.value))
    }

    private fun startGame(topicWords: GameTopicWords) {
        val roundWord = faker.random.randomValue(topicWords.words)

        savedStateHandle.updateStateFlow(GAME_CONTROL_STATE_KEY, gameControlState) {
            GameControlState(
                gameState = GameState.STARTED,
                topicWords = topicWords,
                totalScore = 0,
                round = 1,
                roundWord = roundWord,
                scrambledRoundWord = roundWord.scramble(),
                hasScoredInRound = false,
                hasSkippedRound = false,
                primaryButtonText = R.string.unscramble_game_submit_primary_btn,
                secondaryButtonText = R.string.unscramble_game_skip_secondary_btn,
            )
        }

        resetGuessState()
    }

    private fun submitGuess() {
        val guessValidationResult = gameFormState.guess.validate()
        gameFormState = gameFormState.copy(
            guessError = guessValidationResult.error?.toPresentationMessage(),
        )

        if (!guessValidationResult.isSuccessful) return

        val guessText = gameFormState.guess.text
        val hasScoredInRound =
            guessText.lowercase() == gameControlState.value.roundWord?.lowercase()
        val currentTotalScore = gameControlState.value.totalScore

        val (newRoundWord, newWords) = pickRandomWordForNextRound()

        if (gameControlState.value.round != LAST_ROUND_NUMBER) {
            savedStateHandle.updateStateFlow(GAME_CONTROL_STATE_KEY, gameControlState) { currentUiState ->
                currentUiState.copy(
                    topicWords = gameControlState.value.topicWords?.copyWith(words = newWords),
                    totalScore = if (hasScoredInRound) currentTotalScore + 10 else currentTotalScore,
                    round = gameControlState.value.round + 1,
                    roundWord = newRoundWord,
                    scrambledRoundWord = newRoundWord.scramble(),
                    hasSkippedRound = false,
                )
            }
        } else {
            savedStateHandle.updateStateFlow(GAME_CONTROL_STATE_KEY, gameControlState) { currentUiState ->
                currentUiState.copy(
                    gameState = GameState.FINISHED,
                    hasSkippedRound = false,
                )
            }
        }

        resetGuessState()
    }

    fun restartGame() = initTopicSelection()

    fun onSecondaryButtonClicked() {
        val gameState = gameControlState.value.gameState

        if (gameState == GameState.STARTED) skipWord()
    }

    private fun skipWord() {
        val (newRoundWord, newWords) = pickRandomWordForNextRound()

        if (gameControlState.value.round != LAST_ROUND_NUMBER) {
            savedStateHandle.updateStateFlow(GAME_CONTROL_STATE_KEY, gameControlState) { currentUiState ->
                currentUiState.copy(
                    topicWords = gameControlState.value.topicWords?.copyWith(words = newWords),
                    round = gameControlState.value.round + 1,
                    roundWord = newRoundWord,
                    scrambledRoundWord = newRoundWord.scramble(),
                    hasScoredInRound = false,
                    hasSkippedRound = true,
                )
            }
        } else {
            savedStateHandle.updateStateFlow(GAME_CONTROL_STATE_KEY, gameControlState) { currentUiState ->
                currentUiState.copy(
                    gameState = GameState.FINISHED,
                    hasScoredInRound = false,
                    hasSkippedRound = true,
                )
            }
        }

        resetGuessState()
    }

    fun quitGame() {
        savedStateHandle.updateStateFlow(GAME_CONTROL_STATE_KEY, gameControlState) { currentUiState ->
            currentUiState.copy(
                gameState = GameState.NOT_STARTED,
                primaryButtonText = R.string.unscramble_game_start_game_primary_btn,
                secondaryButtonText = R.string.unscramble_game_no_secondary_btn,
            )
        }
    }

    private fun pickRandomWordForNextRound(): Pair<String, ImmutableList<String>> {
        val currentWords = gameControlState.value.topicWords?.words ?: persistentListOf()
        val currentRoundWord = gameControlState.value.roundWord

        val newRoundWord = faker.random.randomValue(currentWords)
        val newWords = currentWords.filter { word -> word != currentRoundWord }.toPersistentList()

        return Pair(newRoundWord, newWords)
    }

    private fun resetGuessState() {
        gameFormState = gameFormState.copy(
            guess = Guess(text = ""),
            guessError = null,
        )
    }

    companion object {
        private const val GAME_CONTROL_STATE_KEY = "gameControlState"
        private const val LAST_ROUND_NUMBER = 10
    }
}
