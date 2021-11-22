package com.zeroboss.scoringscrabble

import android.app.Application
import com.zeroboss.scoringscrabble.di.*
import io.realm.Realm
import io.realm.RealmConfiguration
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class ScoringApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        initRealm()

        startKoin {
            androidLogger()
            androidContext(this@ScoringApplication)
            modules(listOf(
                realmModule,
                repositoryModule,
                scoringSheetModule,
                selectPlayersViewModel,
                scoringViewModelModule
            ))
        }
    }

    private fun initRealm() {
        Realm.init(this@ScoringApplication)
        val config = RealmConfiguration.Builder()
            .name("scrabble-db")
            .deleteRealmIfMigrationNeeded()
            .build()
        Realm.setDefaultConfiguration(config);
    }
}
