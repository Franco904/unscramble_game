package com.example.unscramble_game.core.presentation.utils

import androidx.lifecycle.SavedStateHandle
import kotlinx.coroutines.flow.StateFlow

fun <T> SavedStateHandle.getStoredStateFlow(key: String, defaultValue: T) =
    getStateFlow(key, defaultValue)

fun <T> SavedStateHandle.updateStateFlow(key: String, stateFlow: StateFlow<T>, function: (T) -> T) {
    synchronized(stateFlow) {
        val prevValue = stateFlow.value
        val nextValue = function(prevValue)
        this[key] = nextValue
    }
}
