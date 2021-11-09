package com.zeroboss.scoringscrabble.di

import com.zeroboss.scoringscrabble.data.repositories.PreferencesRepository
import com.zeroboss.scoringscrabble.data.repositories.PreferencesRepositoryImpl
import io.objectbox.BoxStore
import org.koin.dsl.module

private fun providePreferences(
    boxStore: BoxStore
) : PreferencesRepository = PreferencesRepositoryImpl(boxStore)

val appModule = module {
    single { providePreferences(get()) }
}