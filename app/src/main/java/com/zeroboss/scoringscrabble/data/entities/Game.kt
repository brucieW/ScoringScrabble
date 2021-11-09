package com.zeroboss.scoringscrabble.data.entities

import com.zeroboss.scoring500.data.common.LocalDateTimeConverter
import io.objectbox.annotation.Backlink
import io.objectbox.annotation.Convert
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.relation.ToMany
import io.objectbox.relation.ToOne
import java.time.LocalDateTime

@Entity
data class Game(
    @Id
    var id: Long = 0,

    @Convert(converter = LocalDateTimeConverter::class, dbType = Long::class)
    var started: LocalDateTime = LocalDateTime.now(),

    // This is not set until the round has finished. This allows for
    // a round to stretch out over more than one session.
    @Convert(converter = LocalDateTimeConverter::class, dbType = Long::class)
    var finished: LocalDateTime? = null
) {
    var match = ToOne<Match>(this, Game_.match)

////    @Backlink(to = "game")
////    var hands = ToMany<Hand>(this, Game_.hands)
//
//    fun addHand(hand: Hand) {
//        hands.add(hand)
//
//        if (getWinningTeamId() != -1) {
//            finished = LocalDateTime.now()
//        }
//    }
//
//    fun getWinningTeamId(): Int {
//        var teamId = -1
//        val handsTotals = getHandsTotal()
//
//        if (handsTotals.isNotEmpty()) {
//            val totals = handsTotals.last()
//
//            if (totals[0] >= 500 || totals[1] <= -500) {
//                teamId = 0
//            } else if (totals[1] >= 500 || totals[0] <= -500) {
//                teamId = 1
//            }
//        }
//
//        return teamId
//    }
//
//    fun getHandsTotal(): List<List<Int>> {
//        val totals = mutableListOf<List<Int>>()
//
//        hands.forEach { hand ->
//            val values = hand.trump.getTeamScore()
//
//            if (totals.isEmpty()) {
//                totals.add(values)
//            } else {
//                val offset = totals.size - 1
//                values[0] += totals[offset][0]
//                values[1] += totals[offset][1]
//                totals.add(values)
//            }
//        }
//
//        return totals
//    }
//
//    fun displayTotalScores(): String {
//        val handTotals = getHandsTotal()
//        val totals = if (handTotals.isEmpty()) listOf<Int>(0, 0) else handTotals.last()
//
//        return StringBuilder()
//            .append(totals[0])
//            .append(" : ")
//            .append(totals[1]).toString()
//    }
}
