package com.zeroboss.scoringscrabble.presentation.screens.home

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.zeroboss.scoringscrabble.data.common.ActiveStatus.activeMatch
import com.zeroboss.scoringscrabble.data.common.CommonDb.deleteMatch
import com.zeroboss.scoringscrabble.data.common.CommonDb.getAllMatches
import com.zeroboss.scoringscrabble.data.entities.Match
import kotlinx.coroutines.flow.MutableStateFlow

class HomeViewModel() : ViewModel() {

    private val currentMatchCards = getAllMatches()
    private val _matches = MutableStateFlow(currentMatchCards)
    val matches = _matches

    private var _expandedCard = mutableStateOf<Match?>(if (currentMatchCards.size == 1) currentMatchCards.first() else null)
    var expandedCard = _expandedCard

    fun deleteActiveMatch() {
        _matches.value.remove(activeMatch)
        deleteMatch(activeMatch!!)
    }

    fun deleteActiveGame() {
        _matches.value.remove(activeMatch)
        deleteMatch(activeMatch!!)
    }

    fun onCardArrowClicked(match: Match) {
        _expandedCard.value = if (_expandedCard.value == match) null else match
    }
}