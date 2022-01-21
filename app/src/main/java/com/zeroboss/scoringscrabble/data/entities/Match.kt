package com.zeroboss.scoringscrabble.data.entities

import com.zeroboss.scoringscrabble.data.common.LocalDateTimeConverter
import io.objectbox.annotation.Convert
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.relation.ToMany
import java.time.LocalDateTime

@Entity
data class Match(
    @Id
    var id: Long = 0,

    @Convert(converter = LocalDateTimeConverter::class, dbType = Long::class)
    var lastPlayed: LocalDateTime = LocalDateTime.now(),
) {
    lateinit var teams: ToMany<Team>
    lateinit var players: ToMany<Player>
    lateinit var games: ToMany<Game>

    fun isTeamType() : Boolean {
        return teams.size > 0
    }

    fun getPlayerTeamName(index: Int) : String {
        return if (isTeamType()) teams[index].getTeamName() else players[index].name
    }
}
