package com.zeroboss.scoringscrabble.data.entities

import com.zeroboss.scoringscrabble.data.common.LetterAndPositionConverter
import io.objectbox.annotation.Convert
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.relation.ToMany

@Entity
data class TurnData(
    @Id
    var id: Long = 0,

    var playerId: Int = 0,
    var turn: Int = 0,
) {
    @Convert(converter = LetterAndPositionConverter::class, dbType = String::class)
    lateinit var letters: ToMany<LetterAndPosition>
}
