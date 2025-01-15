package com.example.unscramble_game.previousGames.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.unscramble_game.core.data.local.database.entities.intermediates.GameWithTopicAndScoredRoundsCount
import com.example.unscramble_game.previousGames.data.PreviousGamesRepository
import com.example.unscramble_game.previousGames.presentation.models.PreviousGameUiState
import com.example.unscramble_game.previousGames.presentation.models.PreviousGamesTimeFilter
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Calendar

class PreviousGamesViewModel(
    private val repository: PreviousGamesRepository,
) : ViewModel() {
    private val _previousGames =
        MutableStateFlow<Map<String, ImmutableList<PreviousGameUiState>>>(emptyMap())
    val previousGames = _previousGames.asStateFlow()

    private val _currentTimeFilter = MutableStateFlow(PreviousGamesTimeFilter.LAST_7_DAYS)
    private val currentTimeFilter = _currentTimeFilter.asStateFlow()

    private var getPreviousGamesJob: Job? = null

    fun init() {
        viewModelScope.launch {
            currentTimeFilter.collectLatest { filter ->
                getPreviousGamesJob?.cancel()

                val startTimestamp = Calendar.getInstance()
                    .apply { add(Calendar.DATE, filter.timeAgo) }.timeInMillis

                getPreviousGamesJob = launch {
                    repository.getPreviousGames(startTimestamp = startTimestamp)
                        .map { games -> games.getGroupedAndMapped() }
                        .collect { gamesGroupedByDate ->
                            _previousGames.update { gamesGroupedByDate }
                        }
                }
            }
        }
    }

    private fun List<GameWithTopicAndScoredRoundsCount>.getGroupedAndMapped() = this
        .groupBy { game -> game.getDateTitle() }
        .mapValues { (_, games) ->
            games
                .map { game -> PreviousGameUiState.fromGameData(game) }
                .toPersistentList()
        }

    private fun GameWithTopicAndScoredRoundsCount.getDateTitle(): String {
        val now = Calendar.getInstance()
        val gameDate = Calendar.getInstance().apply { timeInMillis = gameStartTimestamp }

        val diffMillis = now.timeInMillis - gameDate.timeInMillis

        val isGameDate24HoursOrMoreLater = diffMillis > TIME_24_HOURS
        if (!isGameDate24HoursOrMoreLater) return "Today"

        val isGameDate48HoursOrMoreLater = diffMillis > TIME_48_HOURS
        if (!isGameDate48HoursOrMoreLater) return "Yesterday"

        val month = getMonthName(gameDate.get(Calendar.MONTH))
        val dayOfMonth = gameDate.get(Calendar.DAY_OF_MONTH)
        val year = gameDate.get(Calendar.YEAR)

        val isGameDateThisYear = year == now.get(Calendar.YEAR)

        return "$month $dayOfMonth${if (!isGameDateThisYear) ", $year" else ""}"
    }

    private fun getMonthName(monthNumber: Int) = mapOf(
        1 to "January",
        2 to "February",
        3 to "March",
        4 to "April",
        5 to "May",
        6 to "June",
        7 to "July",
        8 to "August",
        9 to "September",
        10 to "October",
        11 to "November",
        12 to "December",
    ).run {
        this[monthNumber] ?: throw IllegalArgumentException("Nonexistent month number")
    }

    fun onTimeFilterChange(newFilter: PreviousGamesTimeFilter) {
        if (newFilter != currentTimeFilter.value) {
            _currentTimeFilter.update { newFilter }
        }
    }

    companion object {
        private const val TIME_24_HOURS = 24 * 60 * 60 * 1000
        private const val TIME_48_HOURS = 2 * TIME_24_HOURS
    }
}
