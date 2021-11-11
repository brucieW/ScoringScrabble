package com.zeroboss.scoringscrabble.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.zeroboss.scoringscrabble.data.common.ActiveStatus
import com.zeroboss.scoringscrabble.data.entities.*

class ScoringSheetViewModel(

) : ViewModel() {
    val _players = mutableListOf<Player>()
    private var _availableLetters = ActiveStatus.letterFrequency.map { }

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
                        LetterAndPosition(Letters.get('O'), "C8"),
                        LetterAndPosition(Letters.get('V'), "C9"),
                        LetterAndPosition(Letters.get('E'), "C10"),
                        LetterAndPosition(Letters.get('R'), "C11"),
                        LetterAndPosition(Letters.get('B'), "C12"),
                        LetterAndPosition(Letters.get('I'), "C13"),
                        LetterAndPosition(Letters.get('D'), "C14"),
                    )
                )

                2 -> return TurnData(
                    playerId,
                    turn,
                    listOf(
                        LetterAndPosition(Letters.get('U'), "M2"),
                        LetterAndPosition(Letters.get('G'), "M3"),
                        LetterAndPosition(Letters.get('A'), "M4"),
                        LetterAndPosition(Letters.get('R'), "M5"),
                    )
                )

                3 -> return TurnData(
                    playerId,
                    turn,
                    listOf(
                        LetterAndPosition(Letters.get('D'), "C9"),
                        LetterAndPosition(Letters.get('E'), "C10"),
                        LetterAndPosition(Letters.get('A'), "C11"),
                    )
                )

                4 -> return TurnData(
                    playerId,
                    turn,
                    listOf(
                        LetterAndPosition(Letters.get('U'), "F9"),
                        LetterAndPosition(Letters.get('R'), "F10"),
                        LetterAndPosition(Letters.get('A'), "F11"),
                        LetterAndPosition(Letters.get('L'), "F12"),
                        LetterAndPosition(Letters.get('I'), "F13"),
                        LetterAndPosition(Letters.get('S'), "F14"),
                        LetterAndPosition(Letters.get('E'), "F15"),
                    )
                )

                5 -> return TurnData(
                    playerId,
                    turn,
                    listOf(
                        LetterAndPosition(Letters.get('X'), "B14"),
                        LetterAndPosition(Letters.get('E'), "B15"),
                    )
                )

                6 -> return TurnData(
                    playerId,
                    turn,
                    listOf(
                        LetterAndPosition(Letters.get('H'), "L12"),
                        LetterAndPosition(Letters.get('O'), "L13"),
                        LetterAndPosition(Letters.get('O'), "L14"),
                        LetterAndPosition(Letters.get('K'), "L15"),
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
                            LetterAndPosition(Letters.get('U'), "11"),
                            LetterAndPosition(Letters.get('N'), "I2"),
                            LetterAndPosition(Letters.get('V'), "I3"),
                            LetterAndPosition(Letters.get('E'), "I4"),
                            LetterAndPosition(Letters.get('I'), "I5"),
                            LetterAndPosition(Letters.get('L'), "I6"),
                            LetterAndPosition(Letters.get('E'), "I7"),
                            LetterAndPosition(Letters.get('D'), "I8"),
                        )
                    )
                }

                2 -> {
                    return TurnData(
                        playerId,
                        turn,
                        listOf(
                            LetterAndPosition(Letters.get('T'), "N2"),
                            LetterAndPosition(Letters.get('O'), "N3"),
                            LetterAndPosition(Letters.get('D'), "N4"),
                        )
                    )
                }

                3 -> return TurnData(
                    playerId,
                    turn,
                    listOf(
                        LetterAndPosition(Letters.get('P'), "A9"),
                        LetterAndPosition(Letters.get('O'), "B9"),
                    )
                )

                4 -> return TurnData(
                    playerId,
                    turn,
                    listOf(
                        LetterAndPosition(Letters.get('F'), "E11"),
                        LetterAndPosition(Letters.get('I'), "G11"),
                        LetterAndPosition(Letters.get('L'), "H11"),
                        LetterAndPosition(Letters.get('U'), "I11"),
                        LetterAndPosition(Letters.get('R'), "J11"),
                        LetterAndPosition(Letters.get('E'), "K11"),
                        LetterAndPosition(Letters.get('S'), "L11"),
                    )
                )

                5 -> return TurnData(
                    playerId,
                    turn,
                    listOf(
                        LetterAndPosition(Letters.get('G'), "A13"),
                        LetterAndPosition(Letters.get('P'), "A15"),
                    )
                )

                6 -> return TurnData(
                    playerId,
                    turn,
                    listOf(
                        LetterAndPosition(Letters.get('W'), "J15"),
                        LetterAndPosition(Letters.get('A'), "K15"),
                        LetterAndPosition(Letters.get('A'), "M15"),
                        LetterAndPosition(Letters.get('M'), "N15"),
                        LetterAndPosition(Letters.get('E'), "O15"),
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
                            LetterAndPosition(Letters.get('Q'), "H1"),
                            LetterAndPosition(Letters.get('B'), "J1"),
                            LetterAndPosition(Letters.get('I'), "K1"),
                            LetterAndPosition(Letters.get('T'), "L1"),
                            LetterAndPosition(Letters.get('S'), "M1"),
                        )
                    )
                }

                2 -> {
                    return TurnData(
                        playerId,
                        turn,
                        listOf(
                            LetterAndPosition(Letters.get('W'), "E5"),
                            LetterAndPosition(Letters.get('O'), "F5"),
                            LetterAndPosition(Letters.get('T'), "G5"),
                            LetterAndPosition(Letters.get('T'), "H5"),
                            LetterAndPosition(Letters.get('N'), "J5"),
                            LetterAndPosition(Letters.get('G'), "K5"),
                        )
                    )
                }

                3 -> return TurnData(
                    playerId,
                    turn,
                    listOf(
                        LetterAndPosition(Letters.get('H'), "B10"),
                        LetterAndPosition(Letters.get('M'), "B11"),
                    )
                )

                4 -> return TurnData(
                    playerId,
                    turn,
                    listOf(
                        LetterAndPosition(Letters.get('X'), "B14"),
                        LetterAndPosition(Letters.get('E'), "C14"),
                        LetterAndPosition(Letters.get('R'), "D14"),
                        LetterAndPosition(Letters.get('O'), "E14"),
                        LetterAndPosition(Letters.get('E'), "G14"),
                        LetterAndPosition(Letters.get('R'), "H14"),
                        LetterAndPosition(Letters.get('E'), "I14"),
                    )
                )

                5 -> return TurnData(
                    playerId,
                    turn,
                    listOf(
                        LetterAndPosition(Letters.get('E'), "C14"),
                        LetterAndPosition(Letters.get('N'), "C15"),
                    )
                )

                6 -> return TurnData(
                    playerId,
                    turn,
                    listOf(
                        LetterAndPosition(Letters.get('J'), "K13"),
                        LetterAndPosition(Letters.get('Y'), "K15"),
                        LetterAndPosition(Letters.get('S'), "K16"),
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
                            LetterAndPosition(Letters.get('L'), "G2"),
                            LetterAndPosition(Letters.get('I'), "H2"),
                            LetterAndPosition(Letters.get('I'), "J2"),
                            LetterAndPosition(Letters.get('N'), "K22"),
                        )
                    )
                }

                2 -> {
                    return TurnData(
                        playerId,
                        turn,
                        listOf(
                            LetterAndPosition(Letters.get('F'), "F6"),
                            LetterAndPosition(Letters.get('A'), "G6"),
                            LetterAndPosition(Letters.get('I'), "H6"),
                        )
                    )
                }

                3 -> {
                    return TurnData(
                        playerId,
                        turn,
                        listOf(
                            LetterAndPosition(Letters.get('C'), "A4"),
                            LetterAndPosition(Letters.get('A'), "A5"),
                            LetterAndPosition(Letters.get('T'), "A6"),
                            LetterAndPosition(Letters.get('N'), "A7"),
                            LetterAndPosition(Letters.get('I'), "A8"),
                        )
                    )
                }

                4 -> return TurnData(
                    playerId,
                    turn,
                    listOf(
                        LetterAndPosition(Letters.get('Y'), "H12"),
                        LetterAndPosition(Letters.get('C'), "H13"),
                        LetterAndPosition(Letters.get('A'), "H15"),
                    )
                )

                5 -> return TurnData(
                    playerId,
                    turn,
                    listOf(
                        LetterAndPosition(Letters.get('D'), "A15"),
                        LetterAndPosition(Letters.get('E'), "D15"),
                    )
                )

                6 -> return TurnData(
                    playerId,
                    turn,
                    listOf(
                        LetterAndPosition(Letters.get('Z'), "N10"),
                        LetterAndPosition(Letters.get('I'), "N11"),
                        LetterAndPosition(Letters.get('N'), "N12"),
                    )
                )
            }
        }
    }

    return TurnData()
}
