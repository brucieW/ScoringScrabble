package com.zeroboss.scoringscrabble.di

import com.zeroboss.scoringscrabble.data.repositories.GamesRepository
import com.zeroboss.scoringscrabble.data.repositories.GamesRepositoryImpl
import org.koin.dsl.module

private fun provideGames(
//    boxStore: BoxStore
) : GamesRepository = GamesRepositoryImpl()

val gamesModule = module {
    single { provideGames() }
}