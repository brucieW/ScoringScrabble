package com.zeroboss.scoringscrabble.data.entities

import com.zeroboss.scoringscrabble.data.common.LocalDateTimeConverter
import io.objectbox.annotation.Convert
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.relation.ToMany
import io.objectbox.relation.ToOne
import java.time.LocalDateTime

@Entity
data class Game(
    @Id
    var id: Long = 0,

    @Convert(converter = LocalDateTimeConverter::class, dbType = Long::class)
    var started: LocalDateTime = LocalDateTime.now(),

    // This is not set until the game has finished. This allows for
    // a game to stretch out over more than one session.
    @Convert(converter = LocalDateTimeConverter::class, dbType = Long::class)
    var finished: LocalDateTime? = null
) {
    lateinit var match: ToOne<Match>
    lateinit var playerTurnData: ToMany<PlayerTurnData>
}
