package com.example.unscramble_game

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner

class InstrumentationTestRunner : AndroidJUnitRunner() {
    override fun newApplication(
        classLoader: ClassLoader?,
        className: String?,
        context: Context?,
    ): Application {
        return super.newApplication(
            classLoader,
            InstrumentationTestApplication::class.java.name,
            context,
        )
    }
}
