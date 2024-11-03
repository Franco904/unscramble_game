package com.example.unscramble_game.core.di

import android.app.Application
import com.example.unscramble_game.core.data.local.database.UnscrambleGameDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(
        application: Application,
    ): UnscrambleGameDatabase {
        return UnscrambleGameDatabase.buildDatabase(
            appContext = application,
        )
    }
}
