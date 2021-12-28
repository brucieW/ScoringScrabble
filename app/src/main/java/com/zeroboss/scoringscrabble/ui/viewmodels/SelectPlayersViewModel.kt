package com.zeroboss.scoringscrabble.ui.viewmodels

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import com.zeroboss.scoringscrabble.data.common.ActiveStatus
import com.zeroboss.scoringscrabble.data.common.CommonDb
import com.zeroboss.scoringscrabble.data.common.CommonDb.getFilteredPlayerNames
import com.zeroboss.scoringscrabble.data.common.CommonDb.getFilteredTeamNames

class SelectPlayersViewModel() : ViewModel()  {

    private val _isTeamType = mutableStateOf<Boolean>(false)
    val isTeamType = _isTeamType

    fun changeUsingTeams() {
        _isTeamType.value = !_isTeamType.value
        checkDataValid()
    }

    val teamListVisible = listOf(
        mutableStateOf(false),
        mutableStateOf(false),
    )

    val playerNames = listOf(
        mutableStateOf(""),
        mutableStateOf(""),
        mutableStateOf(""),
        mutableStateOf("")
    )

    val playerListVisible = listOf(
        mutableStateOf<Boolean>(false),
        mutableStateOf<Boolean>(false),
        mutableStateOf<Boolean>(false),
        mutableStateOf<Boolean>(false)
    )

    private val _uniquePlayerNames = mutableStateOf(true)
    val uniquePlayerNames = _uniquePlayerNames

    private val _dataValid = mutableStateOf(false)
    val dataValid = _dataValid

    fun getFilteredTeams(): List<String> {
        val excludeTeamNames = mutableListOf<String>()

        for (i in 0..3 step 2) {
            val player1 = playerNames[i].value
            val player2 = playerNames[i + 1].value

            if (player1.isNotEmpty() && player2.isNotEmpty()) {
                excludeTeamNames.add("$player1/$player2")
            }
        }

        return getFilteredTeamNames(excludeTeamNames)
    }

    fun getFilteredPlayers(): List<String> {
        val exclude = playerNames.map { name -> name.value }

        return getFilteredPlayerNames(exclude.filter { name -> name.isNotEmpty() } )
    }

    fun checkDataValid() {
        // Check data is valid.

        val itemsWithText = playerNames.filter { name -> name.value.isNotEmpty() }

        _dataValid.value = _isTeamType.value && itemsWithText.size == 4
                || !_isTeamType.value && itemsWithText.size > 1

        val distinct = itemsWithText.distinctBy { it.value.uppercase() }
        _uniquePlayerNames.value = distinct.size == itemsWithText.size
    }

    fun onPlayerNameChanged(
        playerId: Int,
        text: String
    ) {
        playerNames[playerId].value = text
        checkDataValid()
    }

    fun onTeamDropdownClicked(
        teamId: Int,
    ) {
        setTeamDropdownVisible(
            teamId,
            !teamListVisible[teamId].value
        )
    }

    fun onPlayerDropdownClicked(
        playerId: Int
    ) {
        setPlayerDropdownVisible(
            playerId,
            !playerListVisible[playerId].value
        )
    }

    fun onStartScoring() {
        val players = playerNames.map { player -> player.value }.filter { player -> player.isNotEmpty() }
        ActiveStatus.activeMatch = if (isTeamType.value) CommonDb.createTeamsMatch(players)
                        else CommonDb.createPlayersMatch(players)
        ActiveStatus.activePlayer = null
        ActiveStatus.activeTeam = null
    }

    fun setTeamDropdownVisible(
        teamId: Int,
        visible: Boolean
    ) {
        clearAllTeamLists()
        teamListVisible[teamId].value = visible
    }

    private fun clearAllTeamLists() {
        teamListVisible.forEach { it.value = false }
    }

    fun setPlayerDropdownVisible(
        playerId: Int,
        visible: Boolean
    ) {
        clearAllPlayerLists()
        playerListVisible[playerId].value = visible
    }

    fun clearAllPlayerLists() {
        playerListVisible.forEach { it.value = false }
    }

    fun clearPlayerNames() {
        playerNames.forEach { name -> name.value = "" }
    }

    fun setTeamPlayerNames(
        teamId: Int,
        players: List<String>
    ) {
        players.forEachIndexed { index, name ->
            playerNames[(teamId * 2) + index].value = name
        }
    }
}