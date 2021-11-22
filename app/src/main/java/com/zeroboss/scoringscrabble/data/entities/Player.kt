package com.zeroboss.scoringscrabble.data.entities

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Player(
   var name: String = "",

   @PrimaryKey
   var id: Int = 0,
) : RealmObject()

