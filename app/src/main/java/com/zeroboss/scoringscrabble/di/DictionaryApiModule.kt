package com.zeroboss.scoringscrabble.di

import com.zeroboss.scoringscrabble.data.remote.DictionaryApi
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val dictionaryApiModule = module {
    single { provideDictionaryApi() }
}

fun provideDictionaryApi() : DictionaryApi {
    return Retrofit.Builder()
        .baseUrl(DictionaryApi.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(DictionaryApi::class.java)
}
