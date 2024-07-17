package com.example.unscramble_game.core.presentation.validation

import androidx.annotation.StringRes
import com.example.unscramble_game.R
import com.example.unscramble_game.core.domain.validation.GUESS_IS_REQUIRED

object ValidationMessageConverter {
    private val presentationMessages = mapOf(
        GUESS_IS_REQUIRED to R.string.unscramble_input_your_guess_before_submit
    )

    @StringRes
    fun String?.toPresentationMessage(): Int? = presentationMessages[this]
}
