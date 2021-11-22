package com.zeroboss.scoringscrabble.data.entities

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Team(
    var players: RealmList<Player> = RealmList(),

    @PrimaryKey
    var id: Long = 0
) : RealmObject() {

    fun getTeamName() : String {
        return if (players.size < 2) "" else "${players[0]?.name} and ${players[1]?.name}"
    }
}
