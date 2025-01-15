package com.example.unscramble_game.di

import com.example.unscramble_game.core.data.local.database.UnscrambleGameDatabase
import org.koin.dsl.module

val databaseTestModule = module {
    single {
        UnscrambleGameDatabase.buildInMemoryDatabase(appContext = get())
    }
}