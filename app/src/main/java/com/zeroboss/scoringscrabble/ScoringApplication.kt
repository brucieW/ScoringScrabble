package com.zeroboss.scoringscrabble

import android.app.Application
import com.zeroboss.scoringscrabble.di.*
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.logger.Level
import org.koin.core.context.startKoin

class ScoringApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger(if (BuildConfig.DEBUG) Level.ERROR else Level.NONE)
            androidContext(this@ScoringApplication)
            modules(listOf(
                boxStoreModule,
                homeViewModelModule,
                gamesModule,
                selectPlayersViewModel,
                scoringViewModelModule,
                dictionaryApiModule,
                wordInfoViewModule,
                wordInfoUseCaseModule,
                wordInfoRepositoryModule
            ))
        }
    }
}
