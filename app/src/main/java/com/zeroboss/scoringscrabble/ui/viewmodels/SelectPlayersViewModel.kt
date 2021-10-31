package com.zeroboss.scoringscrabble.ui.viewmodels

import androidx.compose.runtime.*
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zeroboss.scoringscrabble.data.ScoringSheetDataRepository
import com.zeroboss.scoringscrabble.data.common.CommonDb.boxStore
import com.zeroboss.scoringscrabble.data.common.CommonDb.getPlayers
import com.zeroboss.scoringscrabble.data.entities.Player
import com.zeroboss.scoringscrabble.data.entities.Team
import io.objectbox.kotlin.boxFor
import kotlinx.coroutines.launch

class SelectPlayersViewModel(
    private val scoringSheetDataRepository: ScoringSheetDataRepository
) : ViewModel()  {
    private val data = scoringSheetDataRepository.getScoringSheetData()

    private val _isTeamType = mutableStateOf<Boolean>(data.isTeamType)
    var isTeamType = _isTeamType

    private val _teamListVisible = createFlags(2)
    var teamListVisible = _teamListVisible

    private val _playerNames = createPlayerNames()
    var playerNames = _playerNames

    private val _playerListVisible = createFlags(4)
    var playerListVisible = _playerListVisible

    private fun createPlayerNames() : MutableList<MutableState<String>> {
        val list = mutableListOf<MutableState<String>>()

        for (i in 0..3) {
            list.add(mutableStateOf(if (i < data.players.size) data.players[i].name else ""))
        }

        return list
    }

    private fun createFlags(count: Int): List<MutableState<Boolean>> {
        val list = mutableListOf<MutableState<Boolean>>()

        for (i in 1..count) {
            list.add(mutableStateOf<Boolean>(false))
        }

        return list
    }

    private val _uniquePlayerNames = mutableStateOf<Boolean>(true)
    var uniquePlayerNames = _uniquePlayerNames

    private val _dataValid = mutableStateOf<Boolean>(false)
    var dataValid = _dataValid

    fun changeUsingTeams() {
        isTeamType.value = !(isTeamType.value)
        checkDataValid()
    }

    fun saveScoringSheetData() {
        viewModelScope.launch {
            data.isTeamType = isTeamType.value
            data.players.clear()
            data.teams.clear()

//            if (data.isTeamType) {
//                teams.forEach { team -> data.teams.add(team) }
//            } else {
//                players.forEach { player -> data.players.add(player) }
//            }

            scoringSheetDataRepository.saveScoringSheetData(data)
        }
    }

    fun clearAllPlayerNames() {
        for (i in 0..3) {
            _playerNames[i].value = ""
        }

        checkDataValid()
    }

    fun getPlayerNameWithOffset(
        offset: Int
    ): MutableState<String> {
        return _playerNames[offset]
    }

    fun isPlayerDropDownVisibleWithOffset(
        offset: Int
    ): Boolean {
        return playerListVisible[offset].value
    }

    fun isTeamDropDownVisibleWithOffset(
        offset: Int
    ): Boolean {
        return teamListVisible[offset].value
    }

    fun onPlayerNameChanged(
        playerId: Int,
        text: String
    ) {
        playerNames[playerId].value = text
        checkDataValid()
    }

    private fun checkDataValid() {
        var valid = true
        var unique = true

        val itemsWithText = playerNames.filter { name -> name.value.isNotEmpty() }

        val minSize = if (isTeamType.value) 4 else 2

        if (itemsWithText.size < minSize) {
            valid = false
        }

        if (itemsWithText.isNotEmpty()) {
            val distinct = itemsWithText.distinctBy { it.value.uppercase() }

            if (distinct.size != itemsWithText.size) {
                unique = false
            }
        }

        _uniquePlayerNames.value = unique
        _dataValid.value = valid
    }

    fun onTeamDropdownClicked(
        teamId: Int,
    ) {
        setTeamDropdownVisible(
            teamId,
            !teamListVisible[teamId].value
        )
    }

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

    fun getFilteredTeamNames(
        exclude: List<String>
    ): List<String> {
        return getTeams()
            .map { team -> team.getTeamName() }.filter { name -> !exclude.contains(name) }
    }

    fun getTeams(): List<Team> {
        return boxStore.boxFor<Team>().all
    }

    fun setPlayerNames(
        teamId: Int,
        playerNames: List<String>
    ) {
        val start = if (teamId == 0) 0 else 1
        val end = if (teamId == 0) 1 else 3

        for (playerId in start..end) {
            onPlayerNameChanged(playerId, playerNames[playerId])
        }
    }

    fun onPlayerDropdownClicked(
        playerId: Int
    ) {
        setPlayerDropdownVisible(
            playerId,
            !playerListVisible[playerId].value
        )
    }

    fun getOffset(
        teamId: Int,
        playerId: Int
    ): Int {
        when (teamId) {
            -1 -> return playerId
            1 -> return (if (playerId == 0) 0 else 1)
            2 -> return (if (playerId == 1) 2 else 3)
            else -> return -1
        }
    }

    fun getFilteredPlayers(): List<String> {
        val exclude = playerNames.map { name -> name.value }

        return getFilteredPlayerNames(exclude.filter { name -> name.isNotEmpty() })
    }

    fun getFilteredPlayerNames(
        exclude: List<String>
    ): List<String> {
        return getPlayers()
            .map { player -> player!!.name }.filter { name -> !exclude.contains(name) }
    }



    fun onStartScoring() {
//        val match = createMatch(
//            players = playerNames.map { it.value }
//        )
//
//        setActiveGame(createGame(match))
//        setActiveMatch(match)
    }

    fun setTeamDropdownVisible(
        teamId: Int,
        visible: Boolean
    ) {
        clearAllTeamLists()
        teamListVisible[teamId].value = visible
    }

    fun clearAllTeamLists() {
//        teamListVisible.forEach { it.value = false }
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
}