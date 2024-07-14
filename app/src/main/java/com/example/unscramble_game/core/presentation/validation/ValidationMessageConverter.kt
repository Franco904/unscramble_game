package com.example.unscramble_game.core.presentation.validation

import androidx.annotation.StringRes
import com.example.unscramble_game.R
import com.example.unscramble_game.core.domain.validation.GUESS_IS_REQUIRED

object ValidationMessageConverter {
    @StringRes
    fun String?.toPresentationMessage(): Int? = when (this) {
        GUESS_IS_REQUIRED -> R.string.unscramble_input_your_guess_before_submit
        else -> null
    }
}
