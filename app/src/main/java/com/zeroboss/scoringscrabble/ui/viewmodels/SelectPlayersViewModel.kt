package com.zeroboss.scoringscrabble.ui.viewmodels

import androidx.compose.runtime.*
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zeroboss.scoringscrabble.data.repositories.ScoreSheetRepository
import com.zeroboss.scoringscrabble.data.common.CommonDb.boxStore
import com.zeroboss.scoringscrabble.data.common.CommonDb.getFilteredPlayerNames
import com.zeroboss.scoringscrabble.data.common.CommonDb.getFilteredTeamNames
import com.zeroboss.scoringscrabble.data.common.CommonDb.getPlayers
import com.zeroboss.scoringscrabble.data.entities.Team
import io.objectbox.kotlin.boxFor
import kotlinx.coroutines.launch

class SelectPlayersViewModel() : ViewModel()  {

    private val _isTeamType = mutableStateOf<Boolean>(false)
    val isTeamType = _isTeamType

    fun changeUsingTeams() {
        _isTeamType.value = !_isTeamType.value
    }

    private val teamListVisible = listOf(
        MutableLiveData<Boolean>(false),
        MutableLiveData<Boolean>(false),
    )

    fun isTeamListVisible(
        offset: Int
    ) : MutableLiveData<Boolean> {
        return teamListVisible[offset]
    }

    val playerNames = listOf(
        MutableLiveData(""),
        MutableLiveData(""),
        MutableLiveData(""),
        MutableLiveData("")
    )

    val playerListVisible = listOf(
        MutableLiveData<Boolean>(false),
        MutableLiveData<Boolean>(false),
        MutableLiveData<Boolean>(false),
        MutableLiveData<Boolean>(false)
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

            if (player1!!.isNotEmpty() && player2!!.isNotEmpty()) {
                excludeTeamNames.add("$player1/$player2")
            }
        }

        return getFilteredTeamNames(excludeTeamNames)
    }

    fun getFilteredPlayers(): List<String> {
        val exclude = playerNames.map { name -> name.value }

        return getFilteredPlayerNames(exclude.filter { name -> name!!.isNotEmpty() } as List<String>)
    }

    private fun getOffset(
        teamId: Int,
        playerId: Int
    ): Int {
        when (teamId) {
            -1 -> return playerId
            1 -> return (if (playerId == 1) 0 else 1)
            2 -> return (if (playerId == 1) 2 else 3)
            else -> return -1
        }
    }

    fun onPlayerNameChanged(
        teamId: Int,
        playerId: Int,
        text: String
    ) {
        playerNames[getOffset(teamId, playerId)].value = text

        // Check data is valid.

        var valid = true
        var unique = true

        val itemsWithText = playerNames.filter { name -> name.value!!.isNotEmpty() }

        if (itemsWithText.size < 4) {
            valid = false
        }

        val distinct = itemsWithText.distinctBy { it.value!!.uppercase() }

        if (distinct.size != itemsWithText.size) {
            unique = false
        }

        _uniquePlayerNames.value = unique
        _dataValid.value = valid
    }

    fun onTeamDropdownClicked(
        teamId: Int,
    ) {
        setTeamDropdownVisible(
            teamId,
            !teamListVisible[teamId - 1].value!!
        )
    }

    fun onPlayerDropdownClicked(
        teamId: Int,
        playerId: Int
    ) {
        setPlayerDropdownVisible(
            teamId,
            playerId,
            !playerListVisible[getOffset(teamId, playerId)].value!!
        )
    }

    fun onStartScoring() {

    }

    fun onClickSave() {
//        val match = createMatch(
//            players = playerNames.map { it.value!! }
//        )
//
//        activeGame = createGame(match)
//        activeMatch = match
    }

    fun setTeamDropdownVisible(
        teamId: Int,
        visible: Boolean
    ) {
        clearAllTeamLists()
        teamListVisible[teamId - 1].value = visible
    }

    private fun clearAllTeamLists() {
        teamListVisible.forEach { it.value = false }
    }

    fun setPlayerDropdownVisible(
        teamId: Int,
        playerId: Int,
        visible: Boolean
    ) {
        clearAllPlayerLists()
        playerListVisible[getOffset(teamId, playerId)].value = visible
    }

    fun clearAllPlayerLists() {
        playerListVisible.forEach { it.value = false }
    }

    fun clearPlayerNames() {
        playerNames.forEach { name -> name.value = "" }
    }

    fun setPlayerNames(
        teamId: Int,
        players: List<String>
    ) {
        players.forEachIndexed { index, name ->
            playerNames[getOffset(teamId, index)].value = name
        }
    }
}