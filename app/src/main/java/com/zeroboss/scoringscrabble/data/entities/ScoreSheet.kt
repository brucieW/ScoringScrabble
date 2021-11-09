package com.zeroboss.scoringscrabble.data.entities

import com.zeroboss.scoring500.data.common.LocalDateTimeConverter
import io.objectbox.annotation.Convert
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.relation.ToMany
import java.time.LocalDateTime

@Entity
data class ScoreSheet(
    @Id
    var id: Long = 0,

    var isTeamType: Boolean = false,

    @Convert(converter = LocalDateTimeConverter::class, dbType = Long::class)
    var started: LocalDateTime = LocalDateTime.now(),

    // This is not set until the game has finished. This allows for
    // a game to stretch out over more than one session.
    @Convert(converter = LocalDateTimeConverter::class, dbType = Long::class)
    var finished: LocalDateTime? = null
) {
    var teams = ToMany<Team>(this, ScoreSheet_.teams)
    var players = ToMany<Player>(this, ScoreSheet_.players)
}
