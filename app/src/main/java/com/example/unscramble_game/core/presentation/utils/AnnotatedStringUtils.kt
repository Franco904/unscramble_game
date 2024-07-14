package com.example.unscramble_game.core.presentation.utils

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.withStyle

fun AnnotatedString.Builder.textSpan(text: String) {
    append(text)
}

fun AnnotatedString.Builder.textSpan(text: TextWithStyle) {
    withStyle(text.style) { append(text.text) }
}

fun AnnotatedString.Builder.textSpan(text: String, style: SpanStyle) {
    withStyle(style) { append(text) }
}

fun String.style(spanStyle: SpanStyle) = TextWithStyle(
    text = this,
    style = spanStyle,
)

data class TextWithStyle(
    val text: String,
    val style: SpanStyle,
)