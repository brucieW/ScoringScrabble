package com.zeroboss.scoringscrabble.data.common

import android.content.Context
import android.widget.Toast
import com.zeroboss.scoringscrabble.data.entities.*
import com.zeroboss.scoringscrabble.ui.viewmodels.Ranking
import com.zeroboss.scoringscrabble.ui.viewmodels.Rankings
import io.objectbox.BoxStore
import io.objectbox.kotlin.boxFor
import io.objectbox.query.Query
import io.objectbox.query.QueryBuilder
import org.koin.core.component.KoinComponent
import org.koin.java.KoinJavaComponent.get
import java.io.File
import java.io.IOException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object CommonDb : KoinComponent {

    val testFile = File("testScrabbble-db")
    val liveFile = File("scoringScrabble-db")

    val boxStore: BoxStore = get(BoxStore::class.java)

    val playersBox = boxStore.boxFor<Player>()
    val teamBox = boxStore.boxFor<Team>()
    val gameBox = boxStore.boxFor<Game>()
    val matchBox = boxStore.boxFor<Match>()
    val playerTurnBox = boxStore.boxFor<PlayerTurnData>()

    private var matchesQuery: Query<Match>? = null
    private var gameQuery: Query<Game>? = null
    private var playerQuery: Query<Player?>? = null

    // This clears all data from the database.
    fun clearBoxStore() {
        boxStore.close()
        BoxStore.deleteAllFiles(File(DATA_PATH))
    }

    fun closeDatabase() {
        boxStore.close()
    }

    fun getMatchesQuery() : Query<Match> {
        if (matchesQuery == null) {
            matchesQuery = matchBox.query().order(Match_.lastPlayed, QueryBuilder.DESCENDING).build()
        }

        return matchesQuery!!
    }

    fun getAllMatches(): MutableList<Match> {
        return getMatchesQuery().find()
    }

    fun getGameQuery() : Query<Game> {
        if (gameQuery == null) {
            gameQuery = gameBox.query().build()
        }

        return gameQuery!!
    }

    /**
     * Return all of the games in the store.
     */
    fun getAllGames(): MutableList<Game> {
        return getGameQuery().find()
    }

    fun createPlayersMatch(
        playerNames: List<String>,
        lastPlayed: LocalDateTime = LocalDateTime.now()
    ): Match {
        // Capitalize names.
        val capitalNames = playerNames.map { name -> name.replaceFirstChar { it.uppercase() } }
        val existingMatch = getMatchWithPlayers(capitalNames)

        if (existingMatch != null) {
            return existingMatch
        }

        val match = Match(lastPlayed = lastPlayed)
        val game = Game()
        game.match.target = match
        capitalNames.forEach { player ->
            match.players.add(getPlayer(player))
        }

        match.games.add(game)
        matchBox.put(match)

        setActiveMatch(match)

        return match
    }

    fun getMatchWithPlayers(
        playerNames: List<String>
    ) : Match? {
        val capitalNames = playerNames.map { name -> name.replaceFirstChar { it.uppercase() } }
        matchBox.all.forEach { match ->
            val matchBoxPlayers = match.players.map { it.name }.filter { capitalNames.contains(it) }

            if (matchBoxPlayers.size == playerNames.size) {
                return match
            }
        }

        return null
    }

    fun createTeamsMatch(
        playerNames: List<String>,
        lastPlayed: LocalDateTime = LocalDateTime.now()
    ): Match {
        // Capitalize names.
        val capitalNames = playerNames.map { name -> name.replaceFirstChar { it.uppercase() } }
        val existingMatch = getMatchWithTeams(capitalNames)

        if (existingMatch != null) {
            return existingMatch
        }

        val match = Match()
        addTeamPlayers(match, 0, capitalNames)
        addTeamPlayers(match, 1, capitalNames)

        val game = Game()
        game.match.target = match
        match.games.add(game)
        match.lastPlayed = lastPlayed

        matchBox.put(match)

        setActiveMatch(match)

        return match
    }

    private fun addTeamPlayers(
        match: Match,
        teamId: Int,
        playerNames: List<String>
    ) {
        val team = Team()
        val start = teamId * 2

        for (playerId in start..start + 1) {
            val player = getPlayer(playerNames[playerId])
            team.players.add(player)
        }

        teamBox.put(team)
        match.teams.add(team)
    }

    fun getMatchWithTeams(
        playerNames: List<String>
    ) : Match? {
        matchBox.all.forEach { match ->
            if (match.teams.isNotEmpty() &&
                    match.teams[0].getTeamName() == "${playerNames[0]}/${playerNames[1]}" &&
                    match.teams[1].getTeamName() == "${playerNames[2]}/${playerNames[3]}") {
                return match
            }
        }

        return null
    }


    fun deleteMatch(match: Match) {
        // Remove all games associated with match
        match.games.forEach {
            deleteGame(it)
        }

        if (match.teams.size > 0) {
            match.teams.forEach { teamBox.remove(it) }
        } else {
            match.players.forEach { playersBox.remove(it) }
        }

        matchBox.remove(match)
    }

    fun deleteGame(game: Game) {
        game.playerTurnData.forEach { playerTurnBox.remove(it) }
        gameBox.remove(game)
    }

    /**
     * Return all of the players in the store.
     */
    fun getPlayers(): MutableList<Player?> {
        val players = playerQuery()!!.find()

        return if (playerQuery == null) mutableListOf() else players
    }

    private fun playerQuery(): Query<Player?>? {
        if (playerQuery == null) {
            playerQuery = boxStore.boxFor<Player>().query().order(Player_.name).build()
        }

        return playerQuery
    }

    fun getPlayer(
        name: String
    ) : Player {
        val players = playersBox.query().equal(Player_.name, name).build().find()
        val player: Player

        if (players.isEmpty()) {
            player = Player(name = name)
            playersBox.put(player)
        } else {
            player = players.first()
        }

        return player
    }

    fun getPlayerCount(): Long {
        return boxStore.boxFor<Player>().count()
    }

    fun getTeamCount(): Long {
        return boxStore.boxFor<Team>().count()
    }

    fun createGame(
        match: Match
    ): Game {
        val game = Game()
        game.match.target = match
        match.games.add(game)
        match.lastPlayed = LocalDateTime.now()
        matchBox.put(match)

        return game
    }

//    fun createHand(
//        game: Game,
//        newTrump: Trump
//    ): Hand {
//        val hand = Hand(trump = newTrump)
//        hand.game.target = game
//        game.addHand(hand)
//
//        boxStore.boxFor<Game>().put(game)
//
//        return hand
//    }

    fun getFilteredTeamNames(
        exclude: List<String>
    ): List<String> {
        return getTeams()
            .map { team -> team.getTeamName() }.filter { name -> !exclude.contains(name) }
    }

    fun getFilteredPlayerNames(
        exclude: List<String>
    ): List<String> {
        return getPlayers()
            .map { player -> player!!.name }.filter { name -> !exclude.contains(name) }
    }

    private fun getTeams(): List<Team> {
        return teamBox.all
    }

//    /**
//     * Get the last match entry.
//     */
//    fun getLastMatch(): FieldClassification.Match? {
//        val matchBox = boxStore.boxFor<FieldClassification.Match>()
//
//        return matchBox.query().order(Match_.lastPlayed, QueryBuilder.DESCENDING).build()
//            .findFirst()
//    }
//
//    fun saveMatch(
//        match: FieldClassification.Match
//    ) {
//        boxStore.boxFor<FieldClassification.Match>().put(match)
//    }
//
//    fun getGames(): List<Game> {
//        return boxStore.boxFor<Game>().all
//    }
//
//    fun getHands(): List<Hand> {
//        return boxStore.boxFor<Hand>().all
//    }
//
//    private fun buildTeamName(
//        names: List<String>
//    ): String {
//        return names[0] + "/" + names[1]
//    }
//
    fun setActiveMatch(
        match: Match
    ) {
        ActiveStatus.activeMatch = match
    }

//    fun setExpandedMatchId(
//        expandedMatchId: Int
//    ) {
//        val status = getActiveStatus()
//        status.expandedMatchId = expandedMatchId
//        boxStore.boxFor<ActiveStatus>().put(status)
//    }
//
    fun getRankings(): Rankings {
        val teamRankings = mutableListOf<Ranking>()
        val playerRankings = mutableListOf<Ranking>()

        getAllMatches().forEach { match ->
//            val ratio = match.getWinLossRatio()
//
//            if (ratio.isNotEmpty()) {
//                val team1 = match.teams[0]
//                val team2 = match.teams[1]
//
//                addTeam(teamRankings, team1.name, ratio[0], ratio[1])
//                addTeam(teamRankings, team2.name, ratio[1], ratio[0])
//
//                addPlayer(playerRankings, team1.players[0].name, ratio[0], ratio[1])
//                addPlayer(playerRankings, team1.players[1].name, ratio[0], ratio[1])
//                addPlayer(playerRankings, team2.players[0].name, ratio[1], ratio[0])
//                addPlayer(playerRankings, team2.players[1].name, ratio[1], ratio[0])
//            }
        }

        sortRankings(teamRankings)
        sortRankings(playerRankings)

        return Rankings(
            teamRankings,
            playerRankings
        )
    }

    private fun sortRankings(rankings: MutableList<Ranking>) {
        rankings.sortWith(CompareRankings)
        setChampion(rankings)
    }

    class CompareRankings {
        companion object : Comparator<Ranking> {
            override fun compare(ranking1: Ranking?, ranking2: Ranking?): Int {
                if (ranking1!!.isNoRanking()) {
                    if (ranking2!!.isNoRanking()) {
                        return ranking1.name.compareTo(ranking2.name)
                    } else {
                        return 1
                    }
                } else if (ranking2!!.isNoRanking()) {
                    return -1
                } else {
                    if (ranking1.wins > ranking2.wins) {
                        return -1
                    }

                    if (ranking1.wins < ranking2.wins) {
                        return 1
                    }

                    // Wins equal, sort according to losses.
                    if (ranking1.losses > ranking2.losses) {
                        return 1
                    }

                    if (ranking1.losses < ranking2.losses) {
                        return -1
                    }
                }

                return 0
            }
        }
    }

//    private fun addTeam(
//        teamRankings: MutableList<Ranking>,
//        name: String,
//        wins: Int,
//        losses: Int
//    ) {
//        val team = teamRankings.find { ranking -> ranking.name.equals(name) }
//
//        if (team == null) {
//            teamRankings.add(Ranking(name, wins, losses))
//        } else {
//            team.wins += wins
//            team.losses += losses
//        }
//    }
//
//    private fun addPlayer(
//        playerRankings: MutableList<Ranking>,
//        name: String,
//        wins: Int,
//        losses: Int
//    ) {
//        val player = playerRankings.find { ranking -> ranking.name.equals(name) }
//
//        if (player == null) {
//            playerRankings.add(Ranking(name, wins, losses))
//        } else {
//            player.wins += wins
//            player.losses += losses
//        }
//    }

    private fun setChampion(rankings: List<Ranking>): List<Ranking> {
        rankings.forEachIndexed { index, ranking ->
            if (index == 0) {
                ranking.champion = true
                ranking.rank = 1
            } else {
                val previousRanking = rankings[index - 1]

                if (ranking.isNoRanking()) {
                    ranking.rank = 0
                } else if (ranking.percentWins == previousRanking.percentWins) {
                    ranking.champion = previousRanking.champion
                    ranking.rank = previousRanking.rank
                } else if (ranking.wins > 0 || ranking.losses > 0) {
                    ranking.rank = previousRanking.rank + 1
                }
            }
        }

        return rankings
    }

    fun backupData(
        context: Context,
        name: String
    ): Boolean {
        return try {
            val dataBaseName = "$DATA_PATH/data.mdb"
            val target = "$BACKUP_PATH/$name/data.mdb"
            File(dataBaseName).copyTo(File(target))
            Toast.makeText(
                context,
                "Backup Saved",
                Toast.LENGTH_LONG
            ).show()
            true
        } catch (ex: IOException) {
            ex.printStackTrace()
            Toast.makeText(
                context,
                "Error, check log",
                Toast.LENGTH_LONG
            ).show()
            false
        }
    }

    data class BackupItem(val index: Int, val date: String)

    fun getBackupFiles(): List<BackupItem> {
        val items = mutableListOf<BackupItem>()

        val fileNames = mutableListOf<String>()

        File(BACKUP_PATH).walk().forEach { file ->
            if (file.isDirectory && !file.name.equals("backups")) {
                fileNames.add(file.name)
            }
        }

        fileNames.sortWith { first, second ->
            val date1 = getDate(first)
            val date2 = getDate(second)

            date2.compareTo(date1)
        }

        fileNames.forEachIndexed { index, fileName ->
            items.add(BackupItem(index, fileName))
        }

        return items
    }

    private fun getDate(
        date: String
    ): LocalDateTime {

        return LocalDateTime.parse(date, DateTimeFormatter.ofPattern(DATE_PATTERN))
    }

    fun clearBackups(
        context: Context
    ): Boolean {
        return try {
            File(BACKUP_PATH).deleteRecursively()
            Toast.makeText(
                context,
                "Backups Cleared",
                Toast.LENGTH_LONG
            ).show()
            true
        } catch (ex: IOException) {
            ex.printStackTrace()
            Toast.makeText(
                context,
                "Error, check log",
                Toast.LENGTH_LONG
            ).show()
            false
        }
    }
}
