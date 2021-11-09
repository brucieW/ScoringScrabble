package com.zeroboss.scoringscrabble.di

import com.zeroboss.scoringscrabble.data.repositories.ScoreSheetRepository
import com.zeroboss.scoringscrabble.data.repositories.ScoreSheetRepositoryImpl
import org.koin.dsl.module

private fun provideScoreSheet(
//    boxStore: BoxStore
) : ScoreSheetRepository = ScoreSheetRepositoryImpl()

val scoringSheetModule = module {
    single { provideScoreSheet() }
}