package com.example.unscramble_game.core.presentation.modifiers

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput

fun Modifier.onClick(onTap: (Offset) -> Unit) = pointerInput(onTap) {
    detectTapGestures(onTap)
}
