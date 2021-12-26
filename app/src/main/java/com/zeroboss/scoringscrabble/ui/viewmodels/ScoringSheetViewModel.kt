package com.zeroboss.scoringscrabble.ui.viewmodels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.ViewModel
import com.zeroboss.scoringscrabble.data.common.ActiveStatus
import com.zeroboss.scoringscrabble.data.entities.*
import com.zeroboss.scoringscrabble.ui.screens.screenData

class ScoringSheetViewModel(

) : ViewModel() {
    val players = mutableListOf<Player>()
    val teams = mutableListOf<Team>()

    private var _availableLetters = ActiveStatus.letterFrequency.map { }

    private val _firstPlayerSelected = mutableStateOf(false)
    val firstPlayerSelected = _firstPlayerSelected

    private val _activePlayer = mutableStateOf(Player())
    val activePlayer = _activePlayer

    fun setActivePlayerByIndex(index: Int) {
        _activePlayer.value = players[index]
    }

    fun setActivePlayer(player: Player) {
        _activePlayer.value = player
        _firstPlayerSelected.value = true
    }

    private val _activeTeam = mutableStateOf(Team())
    val activeTeam = _activeTeam

    fun setActiveTeam(team: Team) {
        _activeTeam.value = team
        _firstPlayerSelected.value = true
    }

    private val _gameStarted = mutableStateOf(false)
    val gameStarted = _gameStarted

    private val _cancelEnabled = mutableStateOf<Float>(0.4f)
    val cancelEnabled = _cancelEnabled

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

    private val _unusedTiles = mutableStateListOf(
        mutableStateOf(0),
        mutableStateOf(0),
        mutableStateOf(0),
        mutableStateOf(0)
    )

    val unusedTiles = _unusedTiles

    fun setUnusedTiles(
        offset: Int,
        value: String
    ) {
        _unusedTiles[offset].value = if (value.isEmpty()) 0 else value.toInt()
    }

    var tileStartX = mutableListOf<Float>()
    var tileStartY = mutableListOf<Float>()

    private val _isFirstPos = mutableStateOf(false)
    val isFirstPos = _isFirstPos
    var currentPos = Offset(0f, 0f)

    var currentLetterPos = Position()
    var currentLetterAndPosition = LetterAndPosition()

    fun setFirstPos(x: Float, y: Float) {
        _isFirstPos.value = false
        val centerAdjustment = screenData.tileWidth / 2 + screenData.tileWidth / 3 + 5

        for (col in 0..14) {
            if (x >= tileStartX[col] && x <= tileStartX[col + 1]) {
                for (row in 0..14) {
                    if (y >= tileStartY[row] && y <= tileStartY[row + 1]) {
                        _isFirstPos.value = true
                        currentPos = Offset(
                            tileStartX[col] + centerAdjustment,
                            tileStartY[row] + centerAdjustment
                        )
                        currentLetterPos = Position(col, row)

                        if (gameTurnData.isEmpty()) {
                            // This is first move. If direction of move is down, then only valid
                            // positions are from H2 to H8. If direction is across, only valid
                            // positions are from B8 to H8.
                            _errorText.value = currentLetterPos.isValidFirstMove(directionSouth.value)
                        }

                        return
                    }
                }
            }
        }
    }

    fun adjustLastStartItems(tileWidth: Float) {
        tileStartX[15] = tileStartX[14] + tileWidth + 2
        tileStartY[15] = tileStartY[14] + tileWidth + 2
    }

    private val _tileCounts = mutableListOf<MutableState<Int>>()
    val tileCounts = _tileCounts

    fun addToTileCount(
        index: Int,
        count: Int
    ) {
        _tileCounts[index].value += count
    }

    var currentTurnId = 1
    var currentTurn = PlayerTurnData()

    val gameTurnData = mutableStateListOf<PlayerTurnData>()

    private val _errorText = mutableStateOf("")
    val errorText = _errorText

    fun addToTurnData(
        letter: Letter,
        blankLetter: Letter? = null
    ) {
        if (currentTurn.turnId == 0) {
            currentTurn.turnId = currentTurnId

            if (activeTeam.value.id == 0L) {
                currentTurn.player.target = activePlayer.value
            } else {
                currentTurn.team.target = activeTeam.value
            }
        }

        currentTurn.letters.add(LetterAndPosition(letter, currentLetterPos, blankLetter))
    }

    fun clickedBackSpace() {
    }

    init {
        if (ActiveStatus.activeMatch!!.isTeamType()) {
            ActiveStatus.activeMatch!!.teams.forEach { team ->
                teams.add(team)
            }

            _activeTeam.value = ActiveStatus.activeMatch!!.teams[0]
        } else {
            ActiveStatus.activeMatch!!.players.forEach { player ->
                players.add(player)
            }
        }

        for (i in 1..16) {
            tileStartX.add(0f)
            tileStartY.add(0f)
        }

        ('A'..'[').forEach {
            _tileCounts.add(mutableStateOf(Letters.get(it).quantity))
        }
    }
}

//private fun getTurnData(
//    playerId: Long,
//    turn: Int
//): MutableState<TurnData> {
//    when (playerId) {
//        1 -> {
//            when (turn) {
//                1 -> return mutableStateOf(
//                    TurnData(
//                        playerId,
//                        turn,
//                        listOf(
//                            LetterAndPosition(Letters.get('O'), convertPosition("C8")),
//                            LetterAndPosition(Letters.get('V'), convertPosition("D8")),
//                            LetterAndPosition(Letters.get('E'), convertPosition("E8")),
//                            LetterAndPosition(Letters.get('R'), convertPosition("F8")),
//                            LetterAndPosition(Letters.get('B'), convertPosition("G8")),
//                            LetterAndPosition(Letters.get('I'), convertPosition("H8")),
//                            LetterAndPosition(Letters.get('D'), convertPosition("I8")),
//                        )
//                    )
//                )
//
//                2 -> return mutableStateOf(
//                    TurnData(
//                        playerId,
//                        turn,
//                        listOf(
//                            LetterAndPosition(Letters.get('U'), convertPosition("M2")),
//                            LetterAndPosition(Letters.get('G'), convertPosition("M3")),
//                            LetterAndPosition(Letters.get('A'), convertPosition("M4")),
//                            LetterAndPosition(Letters.get('R'), convertPosition("M5")),
//                        )
//                    )
//                )
//
//                3 -> return mutableStateOf(
//                    TurnData(
//                        playerId,
//                        turn,
//                        listOf(
//                            LetterAndPosition(Letters.get('D'), convertPosition("C9")),
//                            LetterAndPosition(Letters.get('E'), convertPosition("C10")),
//                            LetterAndPosition(Letters.get('A'), convertPosition("C11")),
//                        )
//                    )
//                )
//
//                4 -> return mutableStateOf(
//                    TurnData(
//                        playerId,
//                        turn,
//                        listOf(
//                            LetterAndPosition(Letters.get('U'), convertPosition("F9")),
//                            LetterAndPosition(Letters.get('R'), convertPosition("F10")),
//                            LetterAndPosition(Letters.get('A'), convertPosition("F11")),
//                            LetterAndPosition(Letters.get('L'), convertPosition("F12")),
//                            LetterAndPosition(Letters.get('I'), convertPosition("F13")),
//                            LetterAndPosition(Letters.get('S'), convertPosition("F14")),
//                            LetterAndPosition(Letters.get('E'), convertPosition("F15")),
//                        )
//                    )
//                )
//
//                5 -> return mutableStateOf(
//                    TurnData(
//                        playerId,
//                        turn,
//                        listOf(
//                            LetterAndPosition(Letters.get('X'), convertPosition("B14")),
//                            LetterAndPosition(Letters.get('E'), convertPosition("B15")),
//                        )
//                    )
//                )
//
//                6 -> return mutableStateOf(
//                    TurnData(
//                        playerId,
//                        turn,
//                        listOf(
//                            LetterAndPosition(Letters.get('H'), convertPosition("L12")),
//                            LetterAndPosition(Letters.get('O'), convertPosition("L13")),
//                            LetterAndPosition(Letters.get('O'), convertPosition("L14")),
//                            LetterAndPosition(Letters.get('K'), convertPosition("L15")),
//                        )
//                    )
//                )
//            }
//        }
//
//        2 -> {
//            when (turn) {
//                1 -> {
//                    return mutableStateOf(
//                        TurnData(
//                            playerId,
//                            turn,
//                            listOf(
//                                LetterAndPosition(Letters.get('U'), convertPosition("I1")),
//                                LetterAndPosition(Letters.get('N'), convertPosition("I2")),
//                                LetterAndPosition(Letters.get('V'), convertPosition("I3")),
//                                LetterAndPosition(Letters.get('E'), convertPosition("I4")),
//                                LetterAndPosition(Letters.get('I'), convertPosition("I5")),
//                                LetterAndPosition(Letters.get('L'), convertPosition("I6")),
//                                LetterAndPosition(Letters.get('E'), convertPosition("I7")),
//                                LetterAndPosition(Letters.get('D'), convertPosition("I8")),
//                            )
//                        )
//                    )
//                }
//
//                2 -> {
//                    return mutableStateOf(TurnData(
//                        playerId,
//                        turn,
//                        listOf(
//                            LetterAndPosition(Letters.get('T'), convertPosition("N2")),
//                            LetterAndPosition(Letters.get('O'), convertPosition("N3")),
//                            LetterAndPosition(Letters.get('D'), convertPosition("N4")),
//                        )
//                    ))
//                }
//
//                3 -> return mutableStateOf(TurnData(
//                    playerId,
//                    turn,
//                    listOf(
//                        LetterAndPosition(Letters.get('P'), convertPosition("A9")),
//                        LetterAndPosition(Letters.get('O'), convertPosition("B9")),
//                    )
//                ))
//
//                4 -> return mutableStateOf(TurnData(
//                    playerId,
//                    turn,
//                    listOf(
//                        LetterAndPosition(Letters.get('F'), convertPosition("E11")),
//                        LetterAndPosition(Letters.get('I'), convertPosition("G11")),
//                        LetterAndPosition(Letters.get('L'), convertPosition("H11")),
//                        LetterAndPosition(Letters.get('U'), convertPosition("I11")),
//                        LetterAndPosition(Letters.get('R'), convertPosition("J11")),
//                        LetterAndPosition(Letters.get('E'), convertPosition("K11")),
//                        LetterAndPosition(Letters.get('S'), convertPosition("L11")),
//                    )
//                ))
//
//                5 -> return mutableStateOf(TurnData(
//                    playerId,
//                    turn,
//                    listOf(
//                        LetterAndPosition(Letters.get('G'), convertPosition("A13")),
//                        LetterAndPosition(Letters.get('P'), convertPosition("A15")),
//                    )
//                ))
//
//                6 -> return mutableStateOf(TurnData(
//                    playerId,
//                    turn,
//                    listOf(
//                        LetterAndPosition(Letters.get('W'), convertPosition("J15")),
//                        LetterAndPosition(Letters.get('A'), convertPosition("K15")),
//                        LetterAndPosition(Letters.get('A'), convertPosition("M15")),
//                        LetterAndPosition(Letters.get('M'), convertPosition("N15")),
//                        LetterAndPosition(Letters.get('E'), convertPosition("O15")),
//                    )
//                ))
//            }
//        }
//
//        3 -> {
//            when (turn) {
//                1 -> {
//                    return mutableStateOf(TurnData(
//                        playerId,
//                        turn,
//                        listOf(
//                            LetterAndPosition(Letters.get('Q'), convertPosition("H1")),
//                            LetterAndPosition(Letters.get('B'), convertPosition("J1")),
//                            LetterAndPosition(Letters.get('['), convertPosition("K1"), Letters.get('I')),
//                            LetterAndPosition(Letters.get('T'), convertPosition("L1")),
//                            LetterAndPosition(Letters.get('S'), convertPosition("M1")),
//                        )
//                    ))
//                }
//
//                2 -> {
//                    return mutableStateOf(TurnData(
//                        playerId,
//                        turn,
//                        listOf(
//                            LetterAndPosition(Letters.get('W'), convertPosition("E5")),
//                            LetterAndPosition(Letters.get('O'), convertPosition("F5")),
//                            LetterAndPosition(Letters.get('T'), convertPosition("G5")),
//                            LetterAndPosition(Letters.get('T'), convertPosition("H5")),
//                            LetterAndPosition(Letters.get('N'), convertPosition("J5")),
//                            LetterAndPosition(Letters.get('G'), convertPosition("K5"))
//                        )
//                    ))
//                }
//
//                3 -> return mutableStateOf(TurnData(
//                    playerId,
//                    turn,
//                    listOf(
//                        LetterAndPosition(Letters.get('H'), convertPosition("B10")),
//                        LetterAndPosition(Letters.get('M'), convertPosition("B11"))
//                    )
//                ))
//
//                4 -> return mutableStateOf(TurnData(
//                    playerId,
//                    turn,
//                    listOf(
//                        LetterAndPosition(Letters.get('X'), convertPosition("B14")),
//                        LetterAndPosition(Letters.get('E'), convertPosition("C14")),
//                        LetterAndPosition(Letters.get('R'), convertPosition("D14")),
//                        LetterAndPosition(Letters.get('O'), convertPosition("E14")),
//                        LetterAndPosition(Letters.get('E'), convertPosition("G14")),
//                        LetterAndPosition(Letters.get('['), convertPosition("H14"), Letters.get('R')),
//                        LetterAndPosition(Letters.get('E'), convertPosition("I14"))
//                    )
//                ))
//
//                5 -> return mutableStateOf(TurnData(
//                    playerId,
//                    turn,
//                    listOf(
//                        LetterAndPosition(Letters.get('E'), convertPosition("C14")),
//                        LetterAndPosition(Letters.get('N'), convertPosition("C15"))
//                    )
//                ))
//
//                6 -> return mutableStateOf(TurnData(
//                    playerId,
//                    turn,
//                    listOf(
//                        LetterAndPosition(Letters.get('J'), convertPosition("K13")),
//                        LetterAndPosition(Letters.get('Y'), convertPosition("M13")),
//                        LetterAndPosition(Letters.get('S'), convertPosition("N13"))
//                    )
//                ))
//            }
//        }
//
//        4 -> {
//            when (turn) {
//                1 -> {
//                    return mutableStateOf(TurnData(
//                        playerId,
//                        turn,
//                        listOf(
//                            LetterAndPosition(Letters.get('L'), convertPosition("G2")),
//                            LetterAndPosition(Letters.get('I'), convertPosition("H2")),
//                            LetterAndPosition(Letters.get('I'), convertPosition("J2")),
//                            LetterAndPosition(Letters.get('N'), convertPosition("K2"))
//                        )
//                    ))
//                }
//
//                2 -> {
//                    return mutableStateOf(TurnData(
//                        playerId,
//                        turn,
//                        listOf(
//                            LetterAndPosition(Letters.get('F'), convertPosition("F6")),
//                            LetterAndPosition(Letters.get('A'), convertPosition("G6")),
//                            LetterAndPosition(Letters.get('I'), convertPosition("H6"))
//                        )
//                    ))
//                }
//
//                3 -> {
//                    return mutableStateOf(TurnData(
//                        playerId,
//                        turn,
//                        listOf(
//                            LetterAndPosition(Letters.get('C'), convertPosition("A4")),
//                            LetterAndPosition(Letters.get('A'), convertPosition("A5")),
//                            LetterAndPosition(Letters.get('T'), convertPosition("A6")),
//                            LetterAndPosition(Letters.get('N'), convertPosition("A7")),
//                            LetterAndPosition(Letters.get('I'), convertPosition("A8"))
//                        )
//                    ))
//                }
//
//                4 -> return mutableStateOf(TurnData(
//                    playerId,
//                    turn,
//                    listOf(
//                        LetterAndPosition(Letters.get('Y'), convertPosition("H12")),
//                        LetterAndPosition(Letters.get('C'), convertPosition("H13")),
//                        LetterAndPosition(Letters.get('A'), convertPosition("H15"))
//                    ))
//                )
//
//                5 -> return mutableStateOf(TurnData(
//                    playerId,
//                    turn,
//                    listOf(
//                        LetterAndPosition(Letters.get('D'), convertPosition("A15")),
//                        LetterAndPosition(Letters.get('E'), convertPosition("D15"))
//                    )
//                ))
//
//                6 -> return mutableStateOf(TurnData(
//                    playerId,
//                    turn,
//                    listOf(
//                        LetterAndPosition(Letters.get('Z'), convertPosition("N10")),
//                        LetterAndPosition(Letters.get('I'), convertPosition("N11")),
//                        LetterAndPosition(Letters.get('N'), convertPosition("N12"))
//                    )
//                ))
//            }
//        }
//    }
//
//    return mutableStateOf(TurnData())
//}
