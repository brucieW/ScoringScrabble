package com.zeroboss.scoringscrabble.data.entities

import com.zeroboss.scoringscrabble.data.common.LetterAndPositionConverter
import io.objectbox.annotation.Convert
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.relation.ToMany
import io.objectbox.relation.ToOne

/**
 * This contains the details of a player's turn details, i.e. which letters have been
 * selected for the turn and which game these letters are associated with.
 */
@Entity
data class PlayerTurnData(
    @Id
    var id: Long = 0,
) {
    lateinit var game: ToOne<Game>
    lateinit var player: ToOne<Player>

    @Convert(converter = LetterAndPositionConverter::class, dbType = String::class)
    lateinit var letters: ToMany<LetterAndPosition>
}
