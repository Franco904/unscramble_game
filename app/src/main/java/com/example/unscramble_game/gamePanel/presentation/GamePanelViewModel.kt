package com.example.unscramble_game.gamePanel.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.unscramble_game.R
import com.example.unscramble_game.core.domain.validation.ValidationResult
import com.example.unscramble_game.core.presentation.utils.scramble
import com.example.unscramble_game.core.presentation.validation.ValidationMessageConverter.toPresentationMessage
import com.example.unscramble_game.gamePanel.domain.useCases.GetAllTopics
import com.example.unscramble_game.gamePanel.presentation.models.GameControlUiState
import com.example.unscramble_game.gamePanel.presentation.models.GameFormUiState
import com.example.unscramble_game.gamePanel.presentation.models.GameStatus
import com.example.unscramble_game.gamePanel.presentation.models.TopicUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GamePanelViewModel @Inject constructor(
    private val getAllTopics: GetAllTopics,
) : ViewModel() {
    /*
     * Business logic attributes
     */
    private val gameWords = mutableListOf<String>()
    private val gameScrambledWords = mutableListOf<String>()
    private val guesses = mutableListOf<String?>()
    private val scores = mutableListOf<Boolean>()
    private val skips = mutableListOf<Boolean>()

    private val remainingWords = mutableListOf<String>()

    private lateinit var topicToWords: Map<String, List<String>>

    /*
     * Screen State/UI logic attributes
     */
    private val _gameControlUiState = MutableStateFlow(GameControlUiState())
    val gameControlUiState: StateFlow<GameControlUiState> = _gameControlUiState

    private val _gameFormUiState = MutableStateFlow(GameFormUiState())
    val gameFormUiState: StateFlow<GameFormUiState> = _gameFormUiState

    private val _topics = MutableStateFlow(listOf<TopicUiState>())
    val topics: StateFlow<List<TopicUiState>> = _topics

    fun init() {
        viewModelScope.launch {
            // TODO: Create event flow for ui events
            val topics = getAllTopics().data ?: throw IllegalStateException("No topics!")

            topicToWords = topics.associate { topic ->
                topic.name to topic.words.map { word -> word.name }
            }

            _topics.update { topics.map { topic -> TopicUiState.fromTopic(topic) } }
        }
    }

    fun onGuessTextChanged(newGuessText: String) {
        _gameFormUiState.update { it.copy(guess = it.guess.copy(value = newGuessText)) }
    }

    fun onGuessFieldFocusChanged(isFocused: Boolean) {
        if (isFocused) {
            _gameFormUiState.update { it.copy(guessError = null) }
            return
        }

        val guessValidationResult = gameFormUiState.value.guess.validate()

        if (guessValidationResult is ValidationResult.Error) {
            _gameFormUiState.update {
                it.copy(guessError = guessValidationResult.error.toPresentationMessage())
            }
        }
    }

    fun onPrimaryButtonClicked() {
        when (gameControlUiState.value.gameStatus) {
            GameStatus.NotStarted -> initTopicSelection()
            GameStatus.TopicSelection -> {}
            GameStatus.Started -> submitGuess()
            GameStatus.Finished -> restartGame()
        }
    }

    private fun initTopicSelection() {
        _gameControlUiState.update {
            it.copy(
                gameStatus = GameStatus.TopicSelection,
                topicName = null,
            )
        }
    }

    fun onCancelTopicSelection() {
        _gameControlUiState.update { it.copy(gameStatus = GameStatus.NotStarted) }
    }

    fun onTopicSelected(selectedTopicName: String?) {
        val topicWords = topicToWords.entries.find { (topicName, _) ->
            topicName == selectedTopicName
        }

        if (topicWords == null) {
            onCancelTopicSelection()
            return
        }

        clearGameHistoryData()
        remainingWords.addAll(topicWords.value)

        startGame(
            topicName = topicWords.key,
            firstRoundWord = remainingWords.first(),
        )
    }

    private fun clearGameHistoryData() {
        gameWords.clear()
        gameScrambledWords.clear()
        guesses.clear()
        scores.clear()
        skips.clear()
        remainingWords.clear()
    }

    private fun startGame(
        topicName: String,
        firstRoundWord: String,
    ) {
        val scrambledFirstRoundWord = firstRoundWord.scramble()

        _gameControlUiState.update {
            GameControlUiState(
                gameStatus = GameStatus.Started,
                topicName = topicName,
                totalScore = 0,
                round = 1,
                scrambledRoundWord = scrambledFirstRoundWord,
                primaryButtonText = R.string.unscramble_game_submit_primary_btn,
                secondaryButtonText = R.string.unscramble_game_skip_secondary_btn,
            )
        }

        resetGuessState()

        gameWords.add(firstRoundWord)
        gameScrambledWords.add(scrambledFirstRoundWord)
    }

    private fun submitGuess() {
        validateRoundGuess(onValidationFailed = { return })

        val currentRoundWord = gameWords.last()
        val hasScoredInRound =
            gameFormUiState.value.guess.value.lowercase() == currentRoundWord.lowercase()
        val currentTotalScore = gameControlUiState.value.totalScore

        if (gameControlUiState.value.round != LAST_ROUND) {
            remainingWords.remove(remainingWords.first())
            val newRoundWord = remainingWords.first()
            val newScrambledRoundWord = newRoundWord.scramble()

            _gameControlUiState.update {
                it.copy(
                    totalScore = if (hasScoredInRound) currentTotalScore + 10 else currentTotalScore,
                    round = gameControlUiState.value.round + 1,
                    scrambledRoundWord = newScrambledRoundWord,
                )
            }

            gameWords.add(newRoundWord)
            gameScrambledWords.add(newScrambledRoundWord)
        } else {
            _gameControlUiState.update { it.copy(gameStatus = GameStatus.Finished) }
        }

        guesses.add(gameFormUiState.value.guess.value)
        resetGuessState()

        scores.add(hasScoredInRound)
        skips.add(false)
    }

    private inline fun validateRoundGuess(onValidationFailed: () -> Unit) {
        val guessValidationResult = gameFormUiState.value.guess.validate()
        _gameFormUiState.update {
            gameFormUiState.value.copy(
                guessError = guessValidationResult.error?.toPresentationMessage(),
            )
        }

        if (guessValidationResult is ValidationResult.Error) onValidationFailed()
    }

    fun restartGame() = initTopicSelection()

    fun onSecondaryButtonClicked() {
        val gameState = gameControlUiState.value.gameStatus

        if (gameState == GameStatus.Started) skipWord()
    }

    private fun skipWord() {
        if (gameControlUiState.value.round != LAST_ROUND) {
            remainingWords.remove(remainingWords.first())
            val newRoundWord = remainingWords.first()
            val newScrambledRoundWord = newRoundWord.scramble()

            _gameControlUiState.update {
                it.copy(
                    round = gameControlUiState.value.round + 1,
                    scrambledRoundWord = newScrambledRoundWord,
                )
            }

            gameWords.add(newRoundWord)
            gameScrambledWords.add(newScrambledRoundWord)
        } else {
            _gameControlUiState.update { it.copy(gameStatus = GameStatus.Finished) }
        }

        guesses.add(null)
        resetGuessState()

        scores.add(false)
        skips.add(true)
    }

    fun quitGame() {
        _gameControlUiState.update {
            it.copy(
                gameStatus = GameStatus.NotStarted,
                primaryButtonText = R.string.unscramble_game_start_game_primary_btn,
                secondaryButtonText = R.string.unscramble_game_no_secondary_btn,
            )
        }
    }

    private fun resetGuessState() {
        _gameFormUiState.update {
            it.copy(
                guess = it.guess.copy(value = ""),
                guessError = null,
            )
        }
    }

    companion object {
        private const val LAST_ROUND = 10
    }
}
