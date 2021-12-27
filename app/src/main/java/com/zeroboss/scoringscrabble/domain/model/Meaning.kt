package com.zeroboss.scoringscrabble.domain.model

data class Meaning(
    val definitions: List<Definition>,
    val partOfSpeech: String
)
