package com.example.unscramble_game.gamePanel.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.unscramble_game.R
import com.example.unscramble_game.core.domain.models.GameTopic
import com.example.unscramble_game.core.domain.models.RoundGuess
import com.example.unscramble_game.core.presentation.utils.scramble
import com.example.unscramble_game.core.presentation.validation.ValidationMessageConverter.toPresentationMessage
import com.example.unscramble_game.gamePanel.presentation.models.GameControlState
import com.example.unscramble_game.gamePanel.presentation.models.GameFormState
import com.example.unscramble_game.gamePanel.presentation.models.GameState
import com.example.unscramble_game.gamePanel.presentation.utils.GameTopicWordsBuilder.getWords
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.persistentMapOf
import kotlinx.coroutines.launch

// TODO: Adicionar SavedStateHandle e converter Compose state em StateFlow
class GamePanelScreenViewModel : ViewModel() {
    private val gameWords = mutableListOf<String>()
    private val gameScrambledWords = mutableListOf<String>()
    private val guesses = mutableListOf<String?>()
    private val scores = mutableListOf<Boolean>()
    private val skips = mutableListOf<Boolean>()

    private val remainingWords = mutableListOf<String>()

    var gameControlState by mutableStateOf(GameControlState())
        private set

    var gameFormState by mutableStateOf(GameFormState())
        private set

    /**
    Must not be changed anywhere other than in the view model class initialization
     */
    private lateinit var topicToWords: ImmutableMap<GameTopic, ImmutableList<String>>

    init {
        // TODO: Carregar lista aleatoriamente partindo de uma lista maior
        //  (ao invés de ir aleatoriamente selecionando a cada round)
        viewModelScope.launch {
            topicToWords = persistentMapOf(
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
        gameFormState = gameFormState.copy(guess = RoundGuess(text = newGuessText))
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
        when (gameControlState.gameState) {
            GameState.NOT_STARTED -> initTopicSelection()
            GameState.TOPIC_SELECTION -> {}
            GameState.STARTED -> submitGuess()
            GameState.FINISHED -> restartGame()
        }
    }

    private fun initTopicSelection() {
        gameControlState = gameControlState.copy(
            gameState = GameState.TOPIC_SELECTION,
            topic = null,
        )
    }

    fun onCancelTopicSelection() {
        gameControlState = gameControlState.copy(gameState = GameState.NOT_STARTED)
    }

    fun onTopicSelected(selectedTopicName: String?) {
        val topicWords =
            topicToWords.entries.find { it.key.description == selectedTopicName }

        if (topicWords == null) {
            onCancelTopicSelection()
            return
        }

        clearGameHistoryData()
        remainingWords.addAll(topicWords.value)

        startGame(
            topic = topicWords.key,
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
        topic: GameTopic,
        firstRoundWord: String,
    ) {
        val scrambledFirstRoundWord = firstRoundWord.scramble()

        gameControlState = GameControlState(
            gameState = GameState.STARTED,
            topic = topic,
            totalScore = 0,
            round = 1,
            scrambledRoundWord = scrambledFirstRoundWord,
            primaryButtonText = R.string.unscramble_game_submit_primary_btn,
            secondaryButtonText = R.string.unscramble_game_skip_secondary_btn,
        )

        resetGuessState()

        gameWords.add(firstRoundWord)
        gameScrambledWords.add(scrambledFirstRoundWord)
    }

    private fun submitGuess() {
        validateRoundGuess(onValidationFailed = { return })

        val currentRoundWord = gameWords.last()
        val hasScoredInRound = gameFormState.guess.text.lowercase() == currentRoundWord.lowercase()
        val currentTotalScore = gameControlState.totalScore

        if (gameControlState.round != LAST_ROUND) {
            remainingWords.remove(remainingWords.first())
            val newRoundWord = remainingWords.first()
            val newScrambledRoundWord = newRoundWord.scramble()

            gameControlState = gameControlState.copy(
                totalScore = if (hasScoredInRound) currentTotalScore + 10 else currentTotalScore,
                round = gameControlState.round + 1,
                scrambledRoundWord = newScrambledRoundWord,
            )

            gameWords.add(newRoundWord)
            gameScrambledWords.add(newScrambledRoundWord)
        } else {
            gameControlState = gameControlState.copy(gameState = GameState.FINISHED)
        }

        guesses.add(gameFormState.guess.text)
        resetGuessState()

        scores.add(hasScoredInRound)
        skips.add(false)
    }

    private inline fun validateRoundGuess(onValidationFailed: () -> Unit) {
        val guessValidationResult = gameFormState.guess.validate()
        gameFormState = gameFormState.copy(
            guessError = guessValidationResult.error?.toPresentationMessage(),
        )

        if (!guessValidationResult.isSuccessful) onValidationFailed()
    }

    fun restartGame() = initTopicSelection()

    fun onSecondaryButtonClicked() {
        val gameState = gameControlState.gameState

        if (gameState == GameState.STARTED) skipWord()
    }

    private fun skipWord() {
        if (gameControlState.round != LAST_ROUND) {
            remainingWords.remove(remainingWords.first())
            val newRoundWord = remainingWords.first()
            val newScrambledRoundWord = newRoundWord.scramble()

            gameControlState = gameControlState.copy(
                round = gameControlState.round + 1,
                scrambledRoundWord = newScrambledRoundWord,
            )

            gameWords.add(newRoundWord)
            gameScrambledWords.add(newScrambledRoundWord)
        } else {
            gameControlState = gameControlState.copy(gameState = GameState.FINISHED)
        }

        guesses.add(null)
        resetGuessState()

        scores.add(false)
        skips.add(true)
    }

    fun quitGame() {
        gameControlState = gameControlState.copy(
            gameState = GameState.NOT_STARTED,
            primaryButtonText = R.string.unscramble_game_start_game_primary_btn,
            secondaryButtonText = R.string.unscramble_game_no_secondary_btn,
        )
    }

    private fun resetGuessState() {
        gameFormState = gameFormState.copy(
            guess = RoundGuess(text = ""),
            guessError = null,
        )
    }

    companion object {
        private const val GAME_CONTROL_STATE_KEY = "gameControlState"
        private const val GAME_FORM_STATE_KEY = "gameFormState"
        private const val LAST_ROUND = 10
    }
}
