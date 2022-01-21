package com.zeroboss.scoringscrabble.data.entities

import io.objectbox.annotation.Backlink
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.relation.ToMany

@Entity
data class Player(
    @Id
    var id: Long = 0,

    var name: String = ""

) {
    lateinit var matches: ToMany<Match>
    lateinit var teams: ToMany<Team>
}
