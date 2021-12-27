package com.zeroboss.scoringscrabble.di

import com.zeroboss.scoringscrabble.data.remote.DictionaryApi
import com.zeroboss.scoringscrabble.data.repositories.WordInfoRepositoryImpl
import com.zeroboss.scoringscrabble.domain.repository.WordInfoRepository
import com.zeroboss.scoringscrabble.domain.use_case.GetWordInfo
import org.koin.dsl.module

private fun provideWordInfoRepository(
    api: DictionaryApi
) : WordInfoRepository = WordInfoRepositoryImpl(api)

val wordInfoRepositoryModule = module {
    single { provideWordInfoRepository(get()) }
}

private fun provideGetWordInfoUseCase(
    repository: WordInfoRepository,
) : GetWordInfo = GetWordInfo(repository)

val wordInfoUseCaseModule = module {
    single { provideGetWordInfoUseCase(get()) }
}



