package com.zeroboss.scoringscrabble.data.entities

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.relation.ToMany

@Entity
data class ScoringSheetData(
    @Id
    var id: Long = 0,

    var isTeamType: Boolean = false
) {
    var teams = ToMany<Team>(this, ScoringSheetData_.teams)
    var players = ToMany<Player>(this, ScoringSheetData_.players)
}
