package com.zeroboss.scoringscrabble.data.common

import com.zeroboss.scoringscrabble.data.entities.*

object ActiveStatus {
    var expandedMatch: Match? = null
    var activeMatch: Match? = null
    var activeGame: Game? = null
    var activeTurnId: Int = 1
    var activePlayerTurnData: PlayerTurnData? = null
    var activeTeamTurnData: TeamTurnData? = null
    var gameTurnData: List<List<PlayerTurnData>> = emptyList()
    var activePlayer: Player? = null
    var activeTeam: Team? = null
    var isTeamGame: Boolean = false
    var letterFrequency: MutableList<Int> = resetLetterFrequency()
}

fun resetLetterFrequency() : MutableList<Int> {
    return mutableListOf(9, 2, 2, 4, 12, 2, 3, 2, 9, 1, 1, 4, 2, 6, 8, 2, 1, 6, 4, 6, 4, 2, 2, 1, 2, 1, 2)
}