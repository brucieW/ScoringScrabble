package com.zeroboss.scoringscrabble

import android.app.Application
import com.zeroboss.scoringscrabble.di.boxStoreModule
import com.zeroboss.scoringscrabble.di.scoringViewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class ScoringApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@ScoringApplication)
            modules(listOf(
                boxStoreModule,
                scoringViewModelModule
            ))
        }
    }
}
