package com.example.unscramble_game.gamePanel.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.unscramble_game.R
import com.example.unscramble_game.core.domain.models.Game
import com.example.unscramble_game.core.domain.models.Round
import com.example.unscramble_game.core.domain.models.Topic
import com.example.unscramble_game.core.domain.models.Word
import com.example.unscramble_game.core.domain.validation.ValidationResult
import com.example.unscramble_game.core.presentation.utils.scramble
import com.example.unscramble_game.core.presentation.validation.ValidationMessageConverter.toPresentationMessage
import com.example.unscramble_game.gamePanel.data.GamePanelRepository
import com.example.unscramble_game.gamePanel.presentation.models.GameControlUiState
import com.example.unscramble_game.gamePanel.presentation.models.GameFormUiState
import com.example.unscramble_game.gamePanel.presentation.models.GameStatus
import com.example.unscramble_game.gamePanel.presentation.models.TopicUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Calendar

class GamePanelViewModel(
    private val repository: GamePanelRepository,
) : ViewModel() {
    /*
     * Business logic attributes
     */
    private var game: Game? = null
    private var rounds = mutableListOf<Round>()
    private val remainingWords = mutableListOf<Word>()

    private lateinit var topicToWords: Map<Topic, List<Word>>

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
            loadGameTopics()
        }
    }

    private suspend fun loadGameTopics() {
        try {
            val topics = repository.getAllTopics()

            topicToWords = topics.associateWith { it.words }

            _topics.update {
                topics.map { topic -> TopicUiState.fromTopic(topic) }
            }
        } catch (e: Exception) {
            e.printStackTrace()

            // TODO: Create event flow for ui events
            // sendUiEvent(UiEvents.Snackbar("Couldn't load game topics"))
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
        val topicWords = topicToWords.entries.find { (topic, _) ->
            topic.name == selectedTopicName
        }

        if (topicWords == null) {
            onCancelTopicSelection()
            return
        }

        val topic = topicWords.key
        val words = topicWords.value

        clearGameControls()
        remainingWords.addAll(words)

        game = Game(
            topic = topic,
            startTimestamp = Calendar.getInstance().timeInMillis,
        )

        startGame(
            topicName = topic.name,
            firstRoundWord = remainingWords.first(),
        )
    }

    private fun clearGameControls() {
        rounds.clear()
        remainingWords.clear()
    }

    private fun startGame(
        topicName: String,
        firstRoundWord: Word,
    ) {
        val scrambledFirstRoundWord = firstRoundWord.name.scramble()

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

        val firstRound = Round(
            number = 1,
            game = game!!,
            word = firstRoundWord,
            scrambledWord = scrambledFirstRoundWord,
        )

        rounds.add(firstRound)
        game = game?.copy(rounds = rounds.toList())
    }

    private fun submitGuess() {
        validateRoundGuess(onValidationFailed = { return })

        val currentRound = game!!.rounds.last()
        val hasScored =
            gameFormUiState.value.guess.value.lowercase() == currentRound.word.name.lowercase()

        rounds[rounds.lastIndex] = currentRound.copy(
            guess = gameFormUiState.value.guess.value,
            isScored = hasScored,
            isSkipped = false,
        )

        val currentTotalScore = gameControlUiState.value.totalScore

        if (gameControlUiState.value.round != LAST_ROUND) {
            remainingWords.remove(remainingWords.first())

            val newRoundNumber = gameControlUiState.value.round + 1
            val newRoundWord = remainingWords.first()
            val newScrambledRoundWord = newRoundWord.name.scramble()

            _gameControlUiState.update {
                it.copy(
                    totalScore = if (hasScored) currentTotalScore + 10 else currentTotalScore,
                    round = newRoundNumber,
                    scrambledRoundWord = newScrambledRoundWord,
                )
            }

            val newRound = Round(
                number = newRoundNumber,
                game = game!!,
                word = newRoundWord,
                scrambledWord = newScrambledRoundWord,
            )

            rounds.add(newRound)
            game = game?.copy(rounds = rounds.toList())
        } else {
            _gameControlUiState.update {
                it.copy(
                    gameStatus = GameStatus.Finished,
                    totalScore = if (hasScored) currentTotalScore + 10 else currentTotalScore,
                )
            }

            onGameFinished()
        }

        resetGuessState()
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
        rounds[rounds.lastIndex] = rounds.last().copy(
            guess = null,
            isScored = false,
            isSkipped = true,
        )

        if (gameControlUiState.value.round != LAST_ROUND) {
            remainingWords.remove(remainingWords.first())

            val newRoundNumber = gameControlUiState.value.round + 1
            val newRoundWord = remainingWords.first()
            val newScrambledRoundWord = newRoundWord.name.scramble()

            _gameControlUiState.update {
                it.copy(
                    round = newRoundNumber,
                    scrambledRoundWord = newScrambledRoundWord,
                )
            }

            val newRound = Round(
                number = newRoundNumber,
                game = game!!,
                word = newRoundWord,
                scrambledWord = newScrambledRoundWord,
            )

            rounds.add(newRound)
            game = game?.copy(rounds = rounds.toList())
        } else {
            _gameControlUiState.update { it.copy(gameStatus = GameStatus.Finished) }
            onGameFinished()
        }

        resetGuessState()
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

    private fun onGameFinished() {
        game = game?.copy(
            endTimestamp = Calendar.getInstance().timeInMillis,
            rounds = rounds,
        )

        viewModelScope.launch {
            try {
                repository.saveGame(game = game!!)
            } catch (e: Exception) {
                e.printStackTrace()

                // TODO: Create event flow for ui events
                // sendUiEvent(UiEvents.Snackbar("Couldn't save last game"))
            }
        }
    }

    companion object {
        private const val LAST_ROUND = 10
    }
}
