package com.zeroboss.scoringscrabble.di

import com.zeroboss.scoringscrabble.data.dao.PlayersDao
import com.zeroboss.scoringscrabble.data.dao.PlayersDaoImpl
import org.koin.dsl.module

val realmModule = module {
    fun providePlayersDao() : PlayersDao { return PlayersDaoImpl() }

    factory { providePlayersDao() }
}

