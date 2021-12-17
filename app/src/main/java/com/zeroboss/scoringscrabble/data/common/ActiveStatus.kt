package com.zeroboss.scoringscrabble.data.common

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import com.zeroboss.scoringscrabble.data.entities.Game
import com.zeroboss.scoringscrabble.data.entities.Match
import com.zeroboss.scoringscrabble.data.entities.Player

object ActiveStatus {
    var expandedMatch: Match? = null
    var activeMatch: Match? = null
    var activeGame: Game? = null
    var isTeamGame: Boolean = false
    var letterFrequency: MutableList<Int> = resetLetterFrequency()
}

fun resetLetterFrequency() : MutableList<Int> {
    return mutableListOf(9, 2, 2, 4, 12, 2, 3, 2, 9, 1, 1, 4, 2, 6, 8, 2, 1, 6, 4, 6, 4, 2, 2, 1, 2, 1, 2)
}