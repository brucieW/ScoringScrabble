package com.zeroboss.scoringscrabble

import android.app.Application
import android.content.Context
import com.zeroboss.scoringscrabble.data.common.CommonDb.testFile
import com.zeroboss.scoringscrabble.data.entities.*
import com.zeroboss.scoringscrabble.di.boxStoreTestModule
import io.objectbox.Box
import io.objectbox.BoxStore
import io.objectbox.kotlin.boxFor
import org.junit.After
import org.junit.Before
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.koin.java.KoinJavaComponent.get

open class BaseBoxStoreTest : KoinTest {
    @Mock
    private lateinit var context: Application

    private lateinit var testStore: BoxStore

    fun getPlayerBox() : Box<Player> {
        return testStore.boxFor(Player::class)
    }

    fun getTeamBox() : Box<Team> {
        return testStore.boxFor(Team::class)
    }

    fun getMatchBox() : Box<Match> {
        return testStore.boxFor(Match::class)
    }

    fun getGameBox() : Box<Game> {
        return testStore.boxFor(Game::class)
    }

    fun getLetterAndPositionBox() : Box<LetterAndPosition> {
        return testStore.boxFor(LetterAndPosition::class)
    }

    @Before
    fun createDatabase() {
        startKoin {
            androidContext(mock(Context::class.java))
            modules(
                listOf(
                    boxStoreTestModule,
                )
            )
        }

        testStore = get(io.objectbox.BoxStore::class.java)
    }

    @After
    fun tearDownDatabase() {
        testStore.close()
        BoxStore.deleteAllFiles(testFile)
        stopKoin()
    }
}