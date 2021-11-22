package com.zeroboss.scoringscrabble.data.dao

import com.zeroboss.scoringscrabble.data.entities.Player
import io.realm.Realm
import io.realm.RealmList

class PlayersDaoImpl : PlayersDao {
    override fun addPlayers(players: List<String>): Boolean {
        return try {
            Realm.getDefaultInstance().executeTransaction { realm ->
                players.forEach { player ->
                    realm.copyToRealm(Player(player))
                }
            }
            true
        } catch (e: Exception) {
            false
        }
    }

    override fun deletePlayers(players: List<Player>): Boolean {
        TODO("Not yet implemented")
    }

    override fun findAll(): List<Player> {
        val realm = Realm.getDefaultInstance()
        val list =  realm.copyFromRealm(realm.where(Player::class.java).findAll())
        realm.close()

        return list
    }

//    var id = 0
//    private fun setUniqueId() : Int {
//        return ++id
//    }
}