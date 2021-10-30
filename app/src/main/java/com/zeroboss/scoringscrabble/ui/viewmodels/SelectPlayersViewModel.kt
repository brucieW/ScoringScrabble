package com.zeroboss.scoringscrabble.ui.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zeroboss.scoringscrabble.data.ScoringSheetDataRepository
import com.zeroboss.scoringscrabble.data.common.CommonDb.getPlayers
import com.zeroboss.scoringscrabble.data.entities.ScoringSheetData_.teams
import kotlinx.coroutines.launch

class SelectPlayersViewModel(
    private val scoringSheetDataRepository: ScoringSheetDataRepository
) : ViewModel()  {
    private val data = scoringSheetDataRepository.getScoringSheetData()

    private val _isTeamType = MutableLiveData<Boolean>(data.isTeamType)
    var isTeamType: MutableLiveData<Boolean> = _isTeamType

    private val _teamListVisible = createFlags(2)

    private val _playerNames = createNames(4)
    val playerNames: List<MutableLiveData<String>> = _playerNames

    private val playerListVisible = createFlags(4)

    private fun createNames(count: Int): List<MutableLiveData<String>> {
        val list = mutableListOf<MutableLiveData<String>>()

        for (i in 1..count) {
            list.add(MutableLiveData<String>(""))
        }

        return list
    }

    private fun createFlags(count: Int): List<MutableLiveData<Boolean>> {
        val list = mutableListOf<MutableLiveData<Boolean>>()

        for (i in 1..count) {
            list.add(MutableLiveData<Boolean>(false))
        }

        return list
    }

    private val _uniquePlayerNames = MutableLiveData<Boolean>(true)
    val uniquePlayerNames: LiveData<Boolean> = _uniquePlayerNames

    private val _dataValid = MutableLiveData<Boolean>(false)
    var dataValid: LiveData<Boolean> = _dataValid

    fun changeUsingTeams() {
        isTeamType.value = !(isTeamType.value)!!
        checkDataValid()
    }

    fun saveScoringSheetData() {
        viewModelScope.launch {
            data.isTeamType = isTeamType.value!!
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
        for (playerId in 0..3) {
            playerNames[playerId].value = ""
        }

        checkDataValid()
    }

    fun getPlayerNameWithOffset(
        offset: Int
    ): MutableLiveData<String> {
        return playerNames[offset]
    }

    fun isPlayerDropDownVisibleWithOffset(
        offset: Int
    ): MutableLiveData<Boolean> {
        return playerListVisible[offset]
    }

    fun onPlayerNameChanged(
        playerId: Int,
        text: String
    ) {
        playerNames[playerId].value = text
        checkDataValid()
    }

    fun checkDataValid() {

        var valid = true
        var unique = true

        val itemsWithText = playerNames.filter { name -> name.value!!.isNotEmpty() }

        val minSize = if (isTeamType.value!!) 4 else 2

        if (itemsWithText.size < minSize) {
            valid = false
        }

        if (itemsWithText.isNotEmpty()) {
            val distinct = itemsWithText.distinctBy { it.value!!.uppercase() }

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
//        setTeamDropdownVisible(
//            teamId,
//            !teamListVisible[teamId - 1].value!!
//        )
    }

    fun onPlayerDropdownClicked(
        playerId: Int
    ) {
        setPlayerDropdownVisible(
            playerId,
            !playerListVisible[playerId].value!!
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
        val exclude = playerNames.map { name -> name.value!! }

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
//            players = playerNames.map { it.value!! }
//        )
//
//        setActiveGame(createGame(match))
//        setActiveMatch(match)
    }

    fun setTeamDropdownVisible(
        teamId: Int,
        visible: Boolean
    ) {
//        clearAllTeamLists()
//        teamListVisible[teamId - 1].value = visible
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