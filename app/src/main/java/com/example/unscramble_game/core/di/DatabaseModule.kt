package com.example.unscramble_game.core.di

import com.example.unscramble_game.core.data.local.database.UnscrambleGameDatabase
import com.example.unscramble_game.core.data.local.database.daos.GameDao
import com.example.unscramble_game.core.data.local.database.daos.RoundDao
import com.example.unscramble_game.core.data.local.database.daos.TopicDao
import org.koin.dsl.module

val databaseModule = module {
    single {
        UnscrambleGameDatabase.buildDatabase(appContext = get())
    }

    single<GameDao> { get<UnscrambleGameDatabase>().gameDao() }
    single<RoundDao> { get<UnscrambleGameDatabase>().roundDao() }
    single<TopicDao> { get<UnscrambleGameDatabase>().topicDao() }
}
