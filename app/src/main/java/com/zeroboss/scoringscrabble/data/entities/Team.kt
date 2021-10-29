package com.zeroboss.scoringscrabble.data.entities

import android.service.autofill.FieldClassification
import io.objectbox.BoxStore
import io.objectbox.annotation.Backlink
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.relation.ToMany
import io.objectbox.relation.ToOne

@Entity
data class Team(
    @Id
    var id: Long = 0,

    var name: String = "",
) {
    var players = ToMany<Player>(this, Team_.players)
}
