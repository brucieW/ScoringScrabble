package com.zeroboss.scoringscrabble.data.dao

import com.zeroboss.scoringscrabble.data.entities.Player
import io.realm.RealmList

interface PlayersDao {
    fun addPlayers(players: List<String>) : Boolean
    fun deletePlayers(players: List<Player>) : Boolean
    fun findAll() : List<Player>
}