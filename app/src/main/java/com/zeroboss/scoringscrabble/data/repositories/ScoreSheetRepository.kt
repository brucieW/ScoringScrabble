package com.zeroboss.scoringscrabble.data.repositories

import com.zeroboss.scoringscrabble.data.common.CommonDb.getAllScoreSheets
import com.zeroboss.scoringscrabble.data.common.CommonDb.scoreSheetBox
import com.zeroboss.scoringscrabble.data.entities.ScoreSheet
import io.objectbox.BoxStore

interface ScoreSheetRepository {
    fun getScoreSheets() : List<ScoreSheet>
    fun saveScoreSheet(scoreSheet: ScoreSheet)
}

class ScoreSheetRepositoryImpl constructor(
) : ScoreSheetRepository {
    override fun getScoreSheets(): MutableList<ScoreSheet> {
        return getAllScoreSheets()
    }

    override fun saveScoreSheet(scoreSheet: ScoreSheet) {
        TODO("Not yet implemented")
    }

}