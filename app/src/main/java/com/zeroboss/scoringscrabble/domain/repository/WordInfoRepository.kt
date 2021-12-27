package com.zeroboss.scoringscrabble.domain.repository

import com.zeroboss.scoringscrabble.core.util.Resource
import com.zeroboss.scoringscrabble.domain.model.WordInfo
import kotlinx.coroutines.flow.Flow

interface WordInfoRepository {

    fun getWordInfo(word: String) : Flow<Resource<List<WordInfo>>>
}