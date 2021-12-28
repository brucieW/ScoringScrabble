package com.zeroboss.scoringscrabble.presentation

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zeroboss.scoringscrabble.core.util.Resource
import com.zeroboss.scoringscrabble.domain.use_case.GetWordInfo
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class WordInfoViewModel(
    private val getWordInfo: GetWordInfo
) : ViewModel() {

    val wordList = mutableListOf(
        mutableStateOf("BANK"),
        mutableStateOf("ALIVE")
    )

    val selectedWords = mutableListOf(
        mutableStateOf(false),
        mutableStateOf(false)
    )

    private val _searchQuery = mutableStateOf("")
    val searchQuery = _searchQuery

    private val _state = mutableStateOf(WordInfoState())
    val state = _state

    private val _errorText = mutableStateOf("")
    val errorText = _errorText

    private var searchJob: Job? = null

    fun clearSearch() {
        _state.value = state.value.copy(
            wordInfoItems = emptyList(),
            isLoading = false
        )

        _errorText.value = ""
    }

    fun onSearch(query: String) {
        _searchQuery.value = query
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            getWordInfo(query)
                .onEach { result ->
                    when (result) {
                        is Resource.Success -> {
                            _state.value = state.value.copy(
                                wordInfoItems = result.data ?: emptyList(),
                                isLoading = false
                            )
                            _errorText.value = ""
                        }

                        is Resource.Error -> {
                            _state.value = state.value.copy(
                                wordInfoItems = result.data ?: emptyList(),
                                isLoading = false
                            )

                            _errorText.value = result.message ?: "Unknown error"
                        }

                        is Resource.Loading -> {
                            _state.value = state.value.copy(
                                wordInfoItems = result.data ?: emptyList(),
                                isLoading = true
                            )

                            _errorText.value = ""
                        }
                    }
                }.launchIn(this)
        }
    }

    sealed class UIEvent {
        data class ShowSnackbar(val message: String) : UIEvent()
    }
}