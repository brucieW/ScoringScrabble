package com.zeroboss.scoringscrabble.di

import com.zeroboss.scoringscrabble.ui.viewmodels.ScoringViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val scoringViewModelModule = module {
    viewModel {
        ScoringViewModel()
    }
}
