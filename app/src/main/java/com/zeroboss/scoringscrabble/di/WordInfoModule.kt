package com.zeroboss.scoringscrabble.di

import com.zeroboss.scoringscrabble.data.remote.DictionaryApi
import com.zeroboss.scoringscrabble.data.repositories.WordInfoRepositoryImpl
import com.zeroboss.scoringscrabble.domain.repository.WordInfoRepository
import org.koin.dsl.module

private fun provideWordInfo(
    api: DictionaryApi
) : WordInfoRepository = WordInfoRepositoryImpl(api)

val wordInfoModule = module {
    single { provideWordInfo(get()) }
}