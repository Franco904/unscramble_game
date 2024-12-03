package com.example.unscramble_game.core.presentation.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

data class SpanTypography(
    val headlineMedium: SpanStyle = SpanStyle(
        fontWeight = FontWeight.W600,
        fontSize = 28.sp,
    ),
    val headlineSmall: SpanStyle = SpanStyle(
        fontWeight = FontWeight.W600,
        fontSize = 24.sp,
    ),
    val titleLarge: SpanStyle = SpanStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 20.sp,
    ),
    val bodyLarge: SpanStyle = SpanStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
    ),
    val bodyMedium: SpanStyle = SpanStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
    ),
    val labelSmall: SpanStyle = SpanStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
    ),
)

private val LocalSpanTypography = staticCompositionLocalOf { SpanTypography() }

val MaterialTheme.spanTypography: SpanTypography
    @Composable
    @ReadOnlyComposable
    get() = LocalSpanTypography.current
