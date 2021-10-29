package com.zeroboss.scoringscrabble.data

import com.zeroboss.scoringscrabble.data.entities.ScoringSheetData
import io.objectbox.BoxStore
import io.objectbox.kotlin.boxFor

interface ScoringSheetDataRepository {
    fun getScoringSheetData() : ScoringSheetData
    fun saveScoringSheetData(scoringSheetData: ScoringSheetData)
}

class ScoringSheetDataRepositoryImpl constructor(
    boxStore: BoxStore
) : ScoringSheetDataRepository {
    private val dataBox = boxStore.boxFor(ScoringSheetData::class)

    override fun getScoringSheetData(): ScoringSheetData {
        return if (dataBox.isEmpty) ScoringSheetData() else dataBox.get(1)
    }

    override fun saveScoringSheetData(scoringSheetData: ScoringSheetData) {
        dataBox.put(scoringSheetData)
    }

}