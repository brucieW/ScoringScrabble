package com.zeroboss.scoringscrabble.presentation

import com.zeroboss.scoringscrabble.domain.model.WordInfo

data class WordInfoState(
    val wordInfoItems: List<WordInfo> = emptyList(),
    val isLoading: Boolean = false
)
