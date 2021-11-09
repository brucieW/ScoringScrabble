package com.zeroboss.scoringscrabble.data.common

import com.zeroboss.scoringscrabble.data.entities.Game
import com.zeroboss.scoringscrabble.data.entities.Match
import com.zeroboss.scoringscrabble.data.entities.Player

object ActiveStatus {
    var expandedMatch: Match? = null
    var activePlayer: Player? = null
    var activeMatch: Match? = null
    var activeGame: Game? = null
}