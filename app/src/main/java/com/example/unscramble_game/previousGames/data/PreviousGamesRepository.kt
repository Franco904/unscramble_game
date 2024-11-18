package com.example.unscramble_game.previousGames.data

import com.example.unscramble_game.core.data.local.database.daos.GameDao

class PreviousGamesRepository(
    private val gameDao: GameDao,
) {
    fun getPreviousGames(startTimestamp: Long) = gameDao.getAllSince(startTimestamp)
}