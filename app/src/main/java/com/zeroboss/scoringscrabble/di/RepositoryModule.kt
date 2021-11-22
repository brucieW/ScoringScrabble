package com.zeroboss.scoringscrabble.di

import android.content.Context
import com.zeroboss.scoringscrabble.data.dao.PlayersDao
import org.koin.dsl.module

val repositoryModule = module {
    fun providePlayersRepository(context: Context, dao: PlayersDao) : PlayersDao {
        return Players
    }
}