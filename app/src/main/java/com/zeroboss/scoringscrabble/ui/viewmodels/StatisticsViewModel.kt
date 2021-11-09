package com.zeroboss.scoringscrabble.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zeroboss.scoringscrabble.data.common.CommonDb.getRankings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class Ranking(
    val name: String,
    var wins: Int,
    var losses: Int,
    var percentWins: Float = 0f,
    var rank: Int = 0,
    var champion: Boolean = false
) {
    init {
        percentWins = if (wins == 0 && losses == 0) 0f else (wins.toFloat() / (wins.toFloat() + losses.toFloat()) * 100f)
    }

    fun isNoRanking() : Boolean {
        return wins == 0 && losses == 0
    }

    fun percentWinsText() : String {
        return if (isNoRanking()) "" else String.format("%.0f", percentWins)
    }
}

data class Rankings(
    val teamRanking: List<Ranking>,
    val playerRanking: List<Ranking>
)

class StatisticsViewModel() : ViewModel() {
    private val rankingsList = getRankings()

    private val _teamRankings = MutableStateFlow(listOf<Ranking>())
    val teamRankings: StateFlow<List<Ranking>> get() = _teamRankings

    private val _playerRankings = MutableStateFlow(listOf<Ranking>())
    val playerRankings: StateFlow<List<Ranking>> get() = _playerRankings

    init {
        viewModelScope.launch(Dispatchers.Default) {
            buildLists()
        }
    }

    private suspend fun buildLists() {
        _teamRankings.emit(rankingsList.teamRanking)
        _playerRankings.emit(rankingsList.playerRanking)
    }
}