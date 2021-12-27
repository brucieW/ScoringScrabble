package com.zeroboss.scoringscrabble.data.repositories

import com.zeroboss.scoringscrabble.core.util.Resource
import com.zeroboss.scoringscrabble.data.remote.DictionaryApi
import com.zeroboss.scoringscrabble.domain.model.WordInfo
import com.zeroboss.scoringscrabble.domain.repository.WordInfoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

class WordInfoRepositoryImpl(
    private val api: DictionaryApi
) : WordInfoRepository {
    override fun getWordInfo(word: String): Flow<Resource<List<WordInfo>>> = flow {
        emit(Resource.Loading())
        var wordInfos = emptyList<WordInfo>()

        try {
            emit(Resource.Success(api.getWordInfo(word).map { it.toWordInfo() }))
        } catch (e: HttpException) {
            emit(
                Resource.Errror(
                    message = "HTTP error: ${e.message()}",
                    data = wordInfos
                )
            )
        } catch (e: IOException) {
            emit(
                Resource.Errror(
                    message = "Couldn't reach server, check internet connection.}",
                    data = wordInfos
                )
            )
        }
    }
}