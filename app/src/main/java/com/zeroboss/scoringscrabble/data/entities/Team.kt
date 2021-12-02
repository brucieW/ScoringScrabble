package com.zeroboss.scoringscrabble.data.entities

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.relation.ToMany

@Entity
data class Team(
    @Id
    var id: Long = 0,
) {
    lateinit var players: ToMany<Player>

    fun getTeamName() : String {
        return if (players.size < 2) "" else "${players[0].name}/${players[1].name}"
    }
}
