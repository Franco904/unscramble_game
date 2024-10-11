package com.example.unscramble_game.gamePanel.di

import android.app.Application
import androidx.room.Room
import com.example.unscramble_game.core.data.local.UnscrambleGameDatabase
import com.example.unscramble_game.core.data.local.UnscrambleGameDatabase.Companion.DB_NAME
import com.example.unscramble_game.gamePanel.data.repository.GamePanelRepositoryImpl
import com.example.unscramble_game.gamePanel.domain.GamePanelRepository
import com.example.unscramble_game.gamePanel.domain.useCases.GetAllTopics
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class GamePanelModule {
    @Provides
    @Singleton
    fun provideGetAllTopics(
        repository: GamePanelRepository,
    ): GetAllTopics {
        return GetAllTopics(repository = repository)
    }

    @Provides
    @Singleton
    fun provideGamePanelRepository(
        db: UnscrambleGameDatabase,
    ): GamePanelRepository {
        return GamePanelRepositoryImpl(topicDao = db.topicDao())
    }

    @Provides
    @Singleton
    fun provideDatabase(
        application: Application,
    ): UnscrambleGameDatabase {
        return Room.databaseBuilder(
            context = application,
            klass = UnscrambleGameDatabase::class.java,
            name = DB_NAME,
        ).build()
    }
}
