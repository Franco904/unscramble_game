package com.example.unscramble_game.core.di

import com.example.unscramble_game.core.data.local.database.UnscrambleGameDatabase
import com.example.unscramble_game.gamePanel.data.repository.GamePanelRepositoryImpl
import com.example.unscramble_game.gamePanel.domain.GamePanelRepository
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
        return GamePanelRepositoryImpl(
            gameDao = db.gameDao(),
            roundDao = db.roundDao(),
            topicDao = db.topicDao(),
        )
    }
}
