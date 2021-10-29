package com.zeroboss.scoringscrabble.di

import com.zeroboss.scoringscrabble.data.ScoringSheetDataRepository
import com.zeroboss.scoringscrabble.data.ScoringSheetDataRepositoryImpl
import io.objectbox.BoxStore
import org.koin.dsl.module

private fun provideScoringSheetData(
    boxStore: BoxStore
) : ScoringSheetDataRepository = ScoringSheetDataRepositoryImpl(boxStore)

val scoringSheetDataModule = module {
    single { provideScoringSheetData(get())}
}