package com.example.unscramble_game.core.di

import com.example.unscramble_game.core.data.local.database.UnscrambleGameDatabase
import com.example.unscramble_game.gamePanel.data.GamePanelRepository
import com.example.unscramble_game.previousGames.data.PreviousGamesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {
    @Provides
    @Singleton
    fun provideGamePanelRepository(
        db: UnscrambleGameDatabase,
    ): GamePanelRepository {
        return GamePanelRepository(
            gameDao = db.gameDao(),
            roundDao = db.roundDao(),
            topicDao = db.topicDao(),
        )
    }

    @Provides
    @Singleton
    fun providePreviousGamesRepository(
        db: UnscrambleGameDatabase,
    ): PreviousGamesRepository {
        return PreviousGamesRepository(
            gameDao = db.gameDao(),
        )
    }
}
