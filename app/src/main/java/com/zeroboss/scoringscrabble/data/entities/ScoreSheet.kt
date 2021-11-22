package com.zeroboss.scoringscrabble.data.entities

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass
import java.time.LocalDateTime

@RealmClass
open class ScoreSheet(
    teams: List<Team> = listOf(),
    players: List<Player> = listOf()
) : RealmObject() {
    @PrimaryKey
    var id: Long = 0

    var teams = teams
    var players = players
    var started: LocalDateTime = LocalDateTime.now()

    // This is not set until the game has finished. This allows for
    // a game to stretch out over more than one session.
    var finished: LocalDateTime? = null

    fun isTeamType() : Boolean {
        return teams.isNotEmpty()
    }
}
