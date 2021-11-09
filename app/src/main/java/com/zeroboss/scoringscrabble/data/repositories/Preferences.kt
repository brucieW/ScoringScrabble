package com.zeroboss.scoringscrabble.data.repositories

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id

@Entity
data class Preferences(
    @Id var id: Long = 0,

)