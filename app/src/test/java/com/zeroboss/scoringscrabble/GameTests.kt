package com.zeroboss.scoringscrabble

import com.zeroboss.scoringscrabble.data.common.ActiveStatus
import com.zeroboss.scoringscrabble.data.common.CommonDb
import com.zeroboss.scoringscrabble.data.common.getDate
import com.zeroboss.scoringscrabble.data.entities.Letter
import com.zeroboss.scoringscrabble.data.entities.LetterAndPosition
import com.zeroboss.scoringscrabble.data.entities.Letters
import com.zeroboss.scoringscrabble.data.entities.Position
import org.junit.Test
import java.time.LocalDateTime

class GameTests : BaseBoxStoreTest() {
    private val player1 = "player1"
    private val player2 = "player2"
    private val player3 = "player3"
    private val player4 = "player4"

    @Test
    fun createPlayerGameTest() {
        val lastPlayed = LocalDateTime.now()

        val match = CommonDb.createPlayersMatch(
            listOf(player1, player2),
            lastPlayed
        )

        val game = match.games[0]
        val player = match.players[0]

        val letterAndPos = LetterAndPosition(Letters.get('X'), Position(10, 10))

        CommonDb.addPlayerLetter(
            game,
            player,
            letterAndPos
        )

        assert(game.match.targetId == match.id)
        assert(game.playerTurnData.size == 1)
        val playerTurnData = game.playerTurnData[0]
        assert(playerTurnData.game.targetId == game.id)
        assert(playerTurnData.letters[0].equals(letterAndPos))

        // Delete match and make sure all boxes have been cleared.
        CommonDb.deleteMatch(match)
        assert(getMatchBox().all.isEmpty())
        assert(getGameBox().all.isEmpty())
        assert(getLetterAndPositionBox().all.isEmpty())
    }

    @Test
    fun createTeamGameTest() {
        val lastPlayed = LocalDateTime.now()

        val match = CommonDb.createTeamsMatch(
            listOf(player1, player2, player3, player4),
            lastPlayed
        )

        val game = match.games[0]
        val team = match.teams[0]

        val letterAndPos = LetterAndPosition(Letters.get('X'), Position(10, 10))

        CommonDb.addTeamLetter(
            game,
            team,
            letterAndPos
        )

        assert(game.match.targetId == match.id)
        assert(game.teamTurnData.size == 1)
        val teamTurnData = game.teamTurnData[0]
        assert(teamTurnData.game.targetId == game.id)
        assert(teamTurnData.letters[0].equals(letterAndPos))

        // Delete match and make sure all boxes have been cleared.
        CommonDb.deleteMatch(match)
        assert(getMatchBox().all.isEmpty())
        assert(getGameBox().all.isEmpty())
        assert(getLetterAndPositionBox().all.isEmpty())
    }
}