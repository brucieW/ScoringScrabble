package com.zeroboss.scoringscrabble.data.entities

import io.objectbox.annotation.Backlink
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.relation.ToMany

/**
 * This contains a player's details, i.e. their name and which matches and teams they
 * have been involved with.
 */
@Entity
data class Player(
    @Id
    var id: Long = 0,

    var name: String = ""

) {
    lateinit var matches: ToMany<Match>
    lateinit var teams: ToMany<Team>
}
