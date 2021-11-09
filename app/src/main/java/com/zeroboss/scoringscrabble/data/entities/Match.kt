package com.zeroboss.scoringscrabble.data.entities

import com.zeroboss.scoring500.data.common.LocalDateTimeConverter
import com.zeroboss.scoringscrabble.ui.screens.Navigation
import io.objectbox.annotation.Backlink
import io.objectbox.annotation.Convert
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.relation.ToMany
import java.time.LocalDateTime

@Entity
data class Match(
    @Id
    var id: Long = 0,

    @Convert(converter = LocalDateTimeConverter::class, dbType = Long::class)
    var lastPlayed: LocalDateTime = LocalDateTime.now()
) {
    var players = ToMany<Player>(this, Match_.players)
    var games = ToMany<Game>(this, Match_.games)
    var scoreSheets = ToMany<ScoreSheet>(this, Match_.scoreSheets)

    fun getWinLossRatioText() : String {
        val ratio = getWinLossRatio()
        var text = "0 : 0"

        if (ratio.isNotEmpty()) {
            val builder = StringBuilder()
                .append(ratio[0])
                .append(" : ")
                .append(ratio[1])

            text = builder.toString()
        }

        return text
    }

    fun getWinLossRatio() : List<Int> {
        val ratio = mutableListOf<Int>(0, 0)

//        scoreSheets.forEach { sheet ->
//            if (sheet.finished != null) {
//                ++ratio[sheet.getWinningTeamId()]
//            }
//        }

        return ratio
    }
}
