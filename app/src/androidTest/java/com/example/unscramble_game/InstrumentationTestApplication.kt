package com.example.unscramble_game

import android.app.Application
import com.example.unscramble_game.core.di.repositoryModule
import com.example.unscramble_game.core.di.viewModelModule
import com.example.unscramble_game.di.databaseTestModule
import org.koin.core.context.startKoin

class InstrumentationTestApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            modules(
                databaseTestModule,
                repositoryModule,
                viewModelModule,
            )
        }
    }
}
