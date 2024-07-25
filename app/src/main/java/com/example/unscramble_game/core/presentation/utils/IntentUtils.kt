package com.example.unscramble_game.core.presentation.utils

import android.content.Context
import android.content.Intent
import android.content.IntentSender

private const val ACTION_SEND_TYPE_TEXT_PLAIN = "text/plain"

private fun Context.showShareSheet(
    intent: Intent,
    title: String? = null,
    sender: IntentSender? = null,
) {
    val shareSheet = Intent.createChooser(intent, title, sender)
    startActivity(shareSheet)
}

fun Context.showTextShareSheet(text: String) {
    val sendIntent = Intent(Intent.ACTION_SEND).apply {
        type = ACTION_SEND_TYPE_TEXT_PLAIN
        putExtra(Intent.EXTRA_TEXT, text)
    }

    showShareSheet(sendIntent)
}