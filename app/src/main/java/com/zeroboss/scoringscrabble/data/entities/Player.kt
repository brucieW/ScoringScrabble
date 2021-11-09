package com.zeroboss.scoringscrabble.data.entities

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id

@Entity
data class Player(
    @Id
    var id: Long = 0,

    var name: String = "",
) {
}
