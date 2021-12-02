package com.zeroboss.scoringscrabble.data.repositories

import com.zeroboss.scoringscrabble.data.common.CommonDb.getAllGames
import com.zeroboss.scoringscrabble.data.entities.Game

interface GamesRepository {
    fun getGames() : List<Game>
    fun saveGame(game: Game)
}

class GamesRepositoryImpl constructor(
) : GamesRepository {
    override fun getGames(): MutableList<Game> {
        return getAllGames()
    }

    override fun saveGame(game: Game) {
        TODO("Not yet implemented")
    }

}