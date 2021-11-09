package com.zeroboss.scoringscrabble.di

import android.content.Context
import android.util.Log
import com.zeroboss.scoringscrabble.BuildConfig
import com.zeroboss.scoringscrabble.data.MyObjectBox
import com.zeroboss.scoringscrabble.data.common.CommonDb.boxStore
import com.zeroboss.scoringscrabble.data.common.CommonDb.testFile
import io.objectbox.BoxStore
import io.objectbox.DebugFlags
import io.objectbox.android.AndroidObjectBrowser
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val boxStoreModule = module {
    single { provideBoxStore(androidContext()) }
}

fun provideBoxStore(
    context: Context
) : BoxStore {

    val boxStore = MyObjectBox
        .builder()
        .name("scoringScrabble")
        .androidContext(context.applicationContext)
        .build()

    if (BuildConfig.DEBUG) {
        Log.i("Object Browser", "Starting")
        val started = AndroidObjectBrowser(boxStore).start(context)
        Log .i("Object Browser", "Started: " + started)
    }

    return boxStore
}

val boxStoreTestModule = module {
    single { provideTestBoxStore() }
}

private fun provideTestBoxStore(): BoxStore {
    if (boxStore == null) {
        return MyObjectBox
            .builder()
            .directory(testFile)
            .debugFlags(DebugFlags.LOG_QUERIES + DebugFlags.LOG_QUERY_PARAMETERS)
            .build()
    } else {
        return boxStore
    }
}

