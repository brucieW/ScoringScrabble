package com.zeroboss.scoringscrabble.data.repositories

import com.zeroboss.scoringscrabble.data.entities.ScoreSheet

interface ScoreSheetRepository {
    fun getScoreSheets() : List<ScoreSheet>
    fun saveScoreSheet(scoreSheet: ScoreSheet)
}

class ScoreSheetRepositoryImpl constructor(
) : ScoreSheetRepository {
    override fun getScoreSheets(): MutableList<ScoreSheet> {
        return mutableListOf() //getAllScoreSheets()
    }

    override fun saveScoreSheet(scoreSheet: ScoreSheet) {
        TODO("Not yet implemented")
    }

}