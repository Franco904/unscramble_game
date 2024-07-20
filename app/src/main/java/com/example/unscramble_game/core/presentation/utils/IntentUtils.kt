package com.example.unscramble_game.core.presentation.utils

import android.content.Context
import android.content.Intent

private const val ACTION_SEND_TYPE_TEXT_PLAIN = "text/plain"

private fun Context.showShareSheet(
    sendIntent: Intent,
    title: String?,
) {
    val shareSheet = Intent.createChooser(sendIntent, title)
    startActivity(shareSheet)
}

fun Context.showTextShareSheet(
    title: String? = null,
    subject: String? = null,
    text: String? = null,
) {
    val sendIntent = Intent(Intent.ACTION_SEND).apply {
        type = ACTION_SEND_TYPE_TEXT_PLAIN

        if (subject != null) putExtra(Intent.EXTRA_SUBJECT, subject)
        if (text != null) putExtra(Intent.EXTRA_TEXT, text)
    }

    showShareSheet(sendIntent, title)
}