package com.zeroboss.scoringscrabble.presentation.screens.scoresheet

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.IntOffset
import androidx.lifecycle.ViewModel
import com.zeroboss.scoringscrabble.data.common.ActiveStatus
import com.zeroboss.scoringscrabble.data.common.ActiveStatus.activePlayerTurnData
import com.zeroboss.scoringscrabble.data.common.CommonDb
import com.zeroboss.scoringscrabble.data.entities.*
import com.zeroboss.scoringscrabble.presentation.common.ScreenData
import kotlinx.coroutines.flow.MutableStateFlow

enum class MoveTileState(var letterAndPosition: LetterAndPosition = LetterAndPosition()) {
    None(),
    Start()
}

class ScoringSheetViewModel(

) : ViewModel() {
    val players = mutableListOf<Player>()
    val teams = mutableListOf<Team>()

    private val _firstPlayerSelected = mutableStateOf(
        ActiveStatus.activePlayer != null || ActiveStatus.activeTeam != null)
    val firstPlayerSelected = _firstPlayerSelected

    private val _activePlayer = mutableStateOf(ActiveStatus.activePlayer)
    val activePlayer = _activePlayer

    private val _activeGame = mutableStateOf(ActiveStatus.activeGame)
    val activeGame = _activeGame

    fun setActivePlayer(player: Player) {
        ActiveStatus.activePlayer = player
        _activePlayer.value = player
        _firstPlayerSelected.value = true

        val turnData = PlayerTurnData()
        turnData.player.target = player
    }

    private val _activeTeam = mutableStateOf(ActiveStatus.activeTeam)
    val activeTeam = _activeTeam

    fun setActiveTeam(team: Team) {
        ActiveStatus.activeTeam = team
        _activeTeam.value = team
        _firstPlayerSelected.value = true

        val turnData = TeamTurnData()
        turnData.team.target = team
    }

    fun isGameStarted() : Boolean {
        return _activePlayer.value != null || _activeTeam.value != null
    }

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

    fun setUnused(
        index: Int,
        unused: Int
    ) {
        _unusedTiles[index].value = unused
        _totalMinusUnused[index].value = _total[index].value - unused
    }

    private val _total = mutableStateListOf(
        mutableStateOf(0),
        mutableStateOf(0),
        mutableStateOf(0),
        mutableStateOf(0)
    )

    val total = _total

    private val _totalMinusUnused = mutableStateListOf(
        mutableStateOf(0),
        mutableStateOf(0),
        mutableStateOf(0),
        mutableStateOf(0)
    )

    val totalMinusUnused = _totalMinusUnused

    var tileStartX = mutableListOf<Float>()
    var tileStartY = mutableListOf<Float>()

    private val _isFirstPos = mutableStateOf(false)
    val isFirstPos = _isFirstPos

    private val _currentPos = mutableStateOf(Offset(-1f, -1f))
    val currentPos = _currentPos

    fun isCurrentPosSet() : Boolean {
        val pos = _currentPos.value

        return (pos.x != -1f && pos.y != -1f)
    }

    private val _isNewLetter = mutableStateOf(false)
    val isNewLetter = _isNewLetter

    private val _newLetterDestination = mutableStateOf(LetterAndPosition())
    val newLetterDestination = _newLetterDestination

    var currentLetterPos = Position()

    fun setFirstPos(x: Float, y: Float) {
        _isFirstPos.value = false
        var centerAdjustment = ScreenData.tileWidth / 2 + 4

        if (ScreenData.isScreenSideways) {
            if (ScreenData.isSmallScreen()) {
                centerAdjustment += 10
            }
        } else if (ScreenData.isSmallScreen()){
            centerAdjustment += 10
        }

        for (col in 0..14) {
            if (x >= tileStartX[col] && x <= tileStartX[col + 1]) {
                for (row in 0..14) {
                    if (y >= tileStartY[row] && y <= tileStartY[row + 1]) {

                        _isFirstPos.value = true
                        _currentPos.value = Offset(
                            tileStartX[col] + centerAdjustment,
                            tileStartY[row] + centerAdjustment
                        )
                        currentLetterPos = Position(col, row)

                        if (gameTurnData.value == null || gameTurnData.value!!.isEmpty()) {
                            // This is first move. If direction of move is down, then only valid
                            // positions are from H2 to H8. If direction is across, only valid
                            // positions are from B8 to H8.
                            _errorText.value = currentLetterPos.isValidFirstMove(directionSouth.value)
                        } else {
                            _errorText.value = ""
                        }

                        return
                    }
                }
            }
        }
    }

    fun moveCurrentPosition(tileWidth: Float) {
        isNewLetter.value = false

        if (directionSouth.value) {
            _currentPos.value = Offset(_currentPos.value.x, _currentPos.value.y + tileWidth + 2)
            currentLetterPos = Position(currentLetterPos.column, currentLetterPos.row + 1)
        } else {
            _currentPos.value = Offset(_currentPos.value.x + tileWidth + 2, _currentPos.value.y)
            currentLetterPos = Position(currentLetterPos.column + 1, currentLetterPos.row)
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

    private val currentTurnData = CommonDb.getPlayerTurnData(
        activePlayer.value,
        activeGame.value)
    private val _gameTurnData = MutableStateFlow(currentTurnData)
    val gameTurnData = _gameTurnData

    private val _errorText = mutableStateOf("")
    val errorText = _errorText

    fun addToPlayerTurnData(
        letter: Letter,
        isBlank: Boolean = false
    ) {
        if (activePlayerTurnData == null) {
            activePlayerTurnData = PlayerTurnData()
        }

        activePlayerTurnData!!.player.target = _activePlayer.value
        _newLetterDestination.value = LetterAndPosition(letter, currentLetterPos, isBlank)
        activePlayerTurnData!!.letters.add(_newLetterDestination.value)
        _isNewLetter.value = true
    }

    fun addToTeamTurnData(
        letter: Letter,
        isBlank: Boolean = false
    ) {
        val turnData = TeamTurnData()
        turnData.team.target = _activeTeam.value
        ActiveStatus.activeTeamTurnData = turnData
        turnData.letters.add(LetterAndPosition(letter, currentLetterPos, isBlank))
    }

    fun clickedBackSpace() {
    }

    fun getBoardOffsetForLetter(
        tile: LetterAndPosition
    ) : IntOffset {
        return IntOffset(
            tileStartX[tile.position.column].toInt(),
            tileStartY[tile.position.row].toInt()
        )
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
