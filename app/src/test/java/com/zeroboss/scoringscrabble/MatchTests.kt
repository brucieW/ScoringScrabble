package com.zeroboss.scoringscrabble

import com.zeroboss.scoringscrabble.data.common.CommonDb
import com.zeroboss.scoringscrabble.data.common.getDate
import org.junit.Test
import java.time.LocalDateTime

class MatchTests : BaseBoxStoreTest() {
    private val player1 = "player1"
    private val player2 = "player2"
    private val player3 = "player3"
    private val player4 = "player4"
    private val player5 = "player5"

    @Test
    fun createMatchTest() {
        val lastPlayed = LocalDateTime.now()

        val match = CommonDb.createPlayersMatch(
            listOf(player1, player2, player3),
            lastPlayed
        )

        assert(getPlayerBox().count() == 3L)
        assert(getDate(match.lastPlayed) == getDate(lastPlayed))
        assert(match.games.size == 1)
        assert(match.games[0].match.target == match)

        getPlayerBox().all.forEachIndexed { index, player ->
            val id = (index + 1).toLong()
            assert(player.id == id)
            assert(player.name == "Player$id")
        }

        val match2 = CommonDb.createPlayersMatch(
            listOf(player2, player4, player5, player3)
        )

        assert(getMatchBox().count() == 2L)
        assert(getPlayerBox().count() == 5L)

        val match3 = CommonDb.createPlayersMatch(
            listOf(player1, player2, player3)
        )

        assert(match3.id == match.id)
        assert(getMatchBox().count() == 2L)

        val match4 = CommonDb.getMatchWithPlayers(
            listOf(player2, player4, player5, player3)
        )

        assert(match4!!.id == match2.id)
        assert(getMatchBox().count() == 2L)

        // Delete match and make sure all boxes have been cleared.
        CommonDb.deleteMatch(match)
        CommonDb.deleteMatch(match2)
        assert(getMatchBox().all.isEmpty())
        assert(getPlayerBox().all.isEmpty())
        assert(getGameBox().all.isEmpty())
    }

    @Test
    fun createTeamsTest() {
        // Create a match using teams
        val match = CommonDb.createTeamsMatch(
            listOf(player1, player2, player3, player4)
        )

        assert(getMatchBox().count() == 1L)
        assert(match.teams.size == 2)
        assert(match.teams[0].getTeamName() == "Player1/Player2")
        assert(match.teams[1].getTeamName() == "Player3/Player4")

        // Try and create a duplicate match
        val newMatch = CommonDb.createTeamsMatch(
            listOf(player1, player2, player3, player4)
        )

        assert(match.id == newMatch.id)

        // Delete match and make sure all boxes have been cleared.
        CommonDb.deleteMatch(match)
        assert(getMatchBox().all.isEmpty())
        assert(getTeamBox().all.isEmpty())
        assert(getPlayerBox().all.isEmpty())
        assert(getGameBox().all.isEmpty())
    }
}