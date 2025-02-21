package com.example.unscramble_game

import android.app.Application
import com.example.unscramble_game.core.di.databaseModule
import com.example.unscramble_game.core.di.repositoryModule
import com.example.unscramble_game.core.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class UnscrambleGameApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@UnscrambleGameApplication)

            modules(
                databaseModule,
                repositoryModule,
                viewModelModule,
            )
        }
    }
}
