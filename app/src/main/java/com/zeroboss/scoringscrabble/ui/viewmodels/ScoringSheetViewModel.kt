package com.zeroboss.scoringscrabble.ui.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.toUpperCase
import androidx.lifecycle.ViewModel
import com.zeroboss.scoringscrabble.data.common.ActiveStatus
import com.zeroboss.scoringscrabble.data.entities.*

class ScoringSheetViewModel(

) : ViewModel() {
    val _players = mutableListOf<Player>()
    private var _availableLetters = ActiveStatus.letterFrequency.map { }

    private val _directionEast = mutableStateOf<Boolean>(true)
    val directionEast = _directionEast

    fun setDirectionEast() {
        _directionEast.value = true
        _directionSouth.value = false
    }

    private val _directionSouth = mutableStateOf<Boolean>(false)
    val directionSouth = _directionSouth

    fun setDirectionSouth() {
        _directionSouth.value = true
        _directionEast.value = false
    }

    private val _firstPos = mutableStateOf("")
    val firstPos = _firstPos

    fun setFirstPos(firstPos: String) {
        _firstPos.value = firstPos
    }

    private val _letters = mutableStateOf("")
    val letters = _letters

    fun setLetters(letters: String) {
        _letters.value = letters.uppercase()
    }

    private val _firstMove = mutableStateOf<Boolean>(true)
    val firstMove = _firstMove

    fun setFirstMove(firstMove: Boolean) {
        _firstMove.value = firstMove
    }

    val turnData = mutableListOf<TurnData>()

    init {
        for (playerId in 1..4) {
            _players.add(Player(name = "Player $playerId"))

            for (turn in 1..5) {
                turnData.add(getTurnData(playerId, turn))
            }
        }
    }

//    private val _matches = MutableStateFlow(currentMatchCards)
//    val matches = _matches

}

private fun getTurnData(
    playerId: Int,
    turn: Int
): TurnData {
    when (playerId) {
        1 -> {
            when (turn) {
                1 -> return TurnData(
                    playerId,
                    turn,
                    listOf(
                        LetterAndPosition(Letters.get('O'), convertPosition("C8")),
                        LetterAndPosition(Letters.get('V'), convertPosition("C9")),
                        LetterAndPosition(Letters.get('E'), convertPosition("C10")),
                        LetterAndPosition(Letters.get('R'), convertPosition("C11")),
                        LetterAndPosition(Letters.get('B'), convertPosition("C12")),
                        LetterAndPosition(Letters.get('I'), convertPosition("C13")),
                        LetterAndPosition(Letters.get('D'), convertPosition("C14")),
                    )
                )

                2 -> return TurnData(
                    playerId,
                    turn,
                    listOf(
                        LetterAndPosition(Letters.get('U'), convertPosition("M2")),
                        LetterAndPosition(Letters.get('G'), convertPosition("M3")),
                        LetterAndPosition(Letters.get('A'), convertPosition("M4")),
                        LetterAndPosition(Letters.get('R'), convertPosition("M5")),
                    )
                )

                3 -> return TurnData(
                    playerId,
                    turn,
                    listOf(
                        LetterAndPosition(Letters.get('D'), convertPosition("C9")),
                        LetterAndPosition(Letters.get('E'), convertPosition("C10")),
                        LetterAndPosition(Letters.get('A'), convertPosition("C11")),
                    )
                )

                4 -> return TurnData(
                    playerId,
                    turn,
                    listOf(
                        LetterAndPosition(Letters.get('U'), convertPosition("F9")),
                        LetterAndPosition(Letters.get('R'), convertPosition("F10")),
                        LetterAndPosition(Letters.get('A'), convertPosition("F11")),
                        LetterAndPosition(Letters.get('L'), convertPosition("F12")),
                        LetterAndPosition(Letters.get('I'), convertPosition("F13")),
                        LetterAndPosition(Letters.get('S'), convertPosition("F14")),
                        LetterAndPosition(Letters.get('E'), convertPosition("F15")),
                    )
                )

                5 -> return TurnData(
                    playerId,
                    turn,
                    listOf(
                        LetterAndPosition(Letters.get('X'), convertPosition("B14")),
                        LetterAndPosition(Letters.get('E'), convertPosition("B15")),
                    )
                )

                6 -> return TurnData(
                    playerId,
                    turn,
                    listOf(
                        LetterAndPosition(Letters.get('H'), convertPosition("L12")),
                        LetterAndPosition(Letters.get('O'), convertPosition("L13")),
                        LetterAndPosition(Letters.get('O'), convertPosition("L14")),
                        LetterAndPosition(Letters.get('K'), convertPosition("L15")),
                    )
                )
            }
        }

        2 -> {
            when (turn) {
                1 -> {
                    return TurnData(
                        playerId,
                        turn,
                        listOf(
                            LetterAndPosition(Letters.get('U'), convertPosition("I1")),
                            LetterAndPosition(Letters.get('N'), convertPosition("I2")),
                            LetterAndPosition(Letters.get('V'), convertPosition("I3")),
                            LetterAndPosition(Letters.get('E'), convertPosition("I4")),
                            LetterAndPosition(Letters.get('I'), convertPosition("I5")),
                            LetterAndPosition(Letters.get('L'), convertPosition("I6")),
                            LetterAndPosition(Letters.get('E'), convertPosition("I7")),
                            LetterAndPosition(Letters.get('D'), convertPosition("I8")),
                        )
                    )
                }

                2 -> {
                    return TurnData(
                        playerId,
                        turn,
                        listOf(
                            LetterAndPosition(Letters.get('T'), convertPosition("N2")),
                            LetterAndPosition(Letters.get('O'), convertPosition("N3")),
                            LetterAndPosition(Letters.get('D'), convertPosition("N4")),
                        )
                    )
                }

                3 -> return TurnData(
                    playerId,
                    turn,
                    listOf(
                        LetterAndPosition(Letters.get('P'), convertPosition("A9")),
                        LetterAndPosition(Letters.get('O'), convertPosition("B9")),
                    )
                )

                4 -> return TurnData(
                    playerId,
                    turn,
                    listOf(
                        LetterAndPosition(Letters.get('F'), convertPosition("E11")),
                        LetterAndPosition(Letters.get('I'), convertPosition("G11")),
                        LetterAndPosition(Letters.get('L'), convertPosition("H11")),
                        LetterAndPosition(Letters.get('U'), convertPosition("I11")),
                        LetterAndPosition(Letters.get('R'), convertPosition("J11")),
                        LetterAndPosition(Letters.get('E'), convertPosition("K11")),
                        LetterAndPosition(Letters.get('S'), convertPosition("L11")),
                    )
                )

                5 -> return TurnData(
                    playerId,
                    turn,
                    listOf(
                        LetterAndPosition(Letters.get('G'), convertPosition("A13")),
                        LetterAndPosition(Letters.get('P'), convertPosition("A15")),
                    )
                )

                6 -> return TurnData(
                    playerId,
                    turn,
                    listOf(
                        LetterAndPosition(Letters.get('W'), convertPosition("J15")),
                        LetterAndPosition(Letters.get('A'), convertPosition("K15")),
                        LetterAndPosition(Letters.get('A'), convertPosition("M15")),
                        LetterAndPosition(Letters.get('M'), convertPosition("N15")),
                        LetterAndPosition(Letters.get('E'), convertPosition("O15")),
                    )
                )
            }
        }

        3 -> {
            when (turn) {
                1 -> {
                    return TurnData(
                        playerId,
                        turn,
                        listOf(
                            LetterAndPosition(Letters.get('Q'), convertPosition("H1")),
                            LetterAndPosition(Letters.get('B'), convertPosition("J1")),
                            LetterAndPosition(Letters.get('I'), convertPosition("K1")),
                            LetterAndPosition(Letters.get('T'), convertPosition("L1")),
                            LetterAndPosition(Letters.get('S'), convertPosition("M1")),
                        )
                    )
                }

                2 -> {
                    return TurnData(
                        playerId,
                        turn,
                        listOf(
                            LetterAndPosition(Letters.get('W'), convertPosition("E5")),
                            LetterAndPosition(Letters.get('O'), convertPosition("F5")),
                            LetterAndPosition(Letters.get('T'), convertPosition("G5")),
                            LetterAndPosition(Letters.get('T'), convertPosition("H5")),
                            LetterAndPosition(Letters.get('N'), convertPosition("J5")),
                            LetterAndPosition(Letters.get('G'), convertPosition("K5"))
                        )
                    )
                }

                3 -> return TurnData(
                    playerId,
                    turn,
                    listOf(
                        LetterAndPosition(Letters.get('H'), convertPosition("B10")),
                        LetterAndPosition(Letters.get('M'), convertPosition("B11"))
                    )
                )

                4 -> return TurnData(
                    playerId,
                    turn,
                    listOf(
                        LetterAndPosition(Letters.get('X'), convertPosition("B14")),
                        LetterAndPosition(Letters.get('E'), convertPosition("C14")),
                        LetterAndPosition(Letters.get('R'), convertPosition("D14")),
                        LetterAndPosition(Letters.get('O'), convertPosition("E14")),
                        LetterAndPosition(Letters.get('E'), convertPosition("G14")),
                        LetterAndPosition(Letters.get('R'), convertPosition("H14")),
                        LetterAndPosition(Letters.get('E'), convertPosition("I14"))
                    )
                )

                5 -> return TurnData(
                    playerId,
                    turn,
                    listOf(
                        LetterAndPosition(Letters.get('E'), convertPosition("C14")),
                        LetterAndPosition(Letters.get('N'), convertPosition("C15"))
                    )
                )

                6 -> return TurnData(
                    playerId,
                    turn,
                    listOf(
                        LetterAndPosition(Letters.get('J'), convertPosition("K13")),
                        LetterAndPosition(Letters.get('Y'), convertPosition("K15")),
                        LetterAndPosition(Letters.get('S'), convertPosition("K16"))
                    )
                )
            }
        }

        4 -> {
            when (turn) {
                1 -> {
                    return TurnData(
                        playerId,
                        turn,
                        listOf(
                            LetterAndPosition(Letters.get('L'), convertPosition("G2")),
                            LetterAndPosition(Letters.get('I'), convertPosition("H2")),
                            LetterAndPosition(Letters.get('I'), convertPosition("J2")),
                            LetterAndPosition(Letters.get('N'), convertPosition("K22"))
                        )
                    )
                }

                2 -> {
                    return TurnData(
                        playerId,
                        turn,
                        listOf(
                            LetterAndPosition(Letters.get('F'), convertPosition("F6")),
                            LetterAndPosition(Letters.get('A'), convertPosition("G6")),
                            LetterAndPosition(Letters.get('I'), convertPosition("H6"))
                        )
                    )
                }

                3 -> {
                    return TurnData(
                        playerId,
                        turn,
                        listOf(
                            LetterAndPosition(Letters.get('C'), convertPosition("A4")),
                            LetterAndPosition(Letters.get('A'), convertPosition("A5")),
                            LetterAndPosition(Letters.get('T'), convertPosition("A6")),
                            LetterAndPosition(Letters.get('N'), convertPosition("A7")),
                            LetterAndPosition(Letters.get('I'), convertPosition("A8"))
                        )
                    )
                }

                4 -> return TurnData(
                    playerId,
                    turn,
                    listOf(
                        LetterAndPosition(Letters.get('Y'), convertPosition("H12")),
                        LetterAndPosition(Letters.get('C'), convertPosition("H13")),
                        LetterAndPosition(Letters.get('A'), convertPosition("H15"))
                    )
                )

                5 -> return TurnData(
                    playerId,
                    turn,
                    listOf(
                        LetterAndPosition(Letters.get('D'), convertPosition("A15")),
                        LetterAndPosition(Letters.get('E'), convertPosition("D15"))
                    )
                )

                6 -> return TurnData(
                    playerId,
                    turn,
                    listOf(
                        LetterAndPosition(Letters.get('Z'), convertPosition("N10")),
                        LetterAndPosition(Letters.get('I'), convertPosition("N11")),
                        LetterAndPosition(Letters.get('N'), convertPosition("N12"))
                    )
                )
            }
        }
    }

    return TurnData()
}
