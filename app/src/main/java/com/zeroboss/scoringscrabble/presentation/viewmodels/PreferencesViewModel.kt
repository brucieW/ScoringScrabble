package com.zeroboss.scoringscrabble.presentation.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.*
import com.zeroboss.scoringscrabble.data.repositories.Preferences
import com.zeroboss.scoringscrabble.data.repositories.PreferencesRepository
import kotlinx.coroutines.launch

class PreferencesViewModel(
    private val preferencesRepository: PreferencesRepository
) : ViewModel() {
    val viewState = mutableStateOf(preferencesRepository.getPreferences())

    fun saveRules(preferences: Preferences) {
//        nonBiddingPointsType = preferences.nonBiddingPointsType
//        isTenTricksBonus = preferences.isTenTricksBonus

        viewModelScope.launch {
            viewState.value = viewState.value.copy(
//                nonBiddingPointsType = preferences.nonBiddingPointsType,
//                isTenTricksBonus = preferences.isTenTricksBonus
            )

            preferencesRepository.savePreferences(preferences)
        }
    }
}