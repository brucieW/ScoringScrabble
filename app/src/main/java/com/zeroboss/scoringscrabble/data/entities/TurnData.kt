package com.zeroboss.scoringscrabble.data.entities

data class TurnData(
    val playerId: Int = 0,
    val turn: Int = 0,
    val letters: List<LetterAndPosition> = listOf()
)

