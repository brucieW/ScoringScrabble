package com.zeroboss.scoringscrabble.di

import com.zeroboss.scoringscrabble.presentation.WordInfoViewModel
import com.zeroboss.scoringscrabble.ui.viewmodels.HomeViewModel
import com.zeroboss.scoringscrabble.ui.viewmodels.ScoringSheetViewModel
import com.zeroboss.scoringscrabble.ui.viewmodels.SelectPlayersViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val homeViewModelModule = module {
    viewModel {
        HomeViewModel()
    }
}

val scoringViewModelModule = module {
    viewModel {
        ScoringSheetViewModel()
    }
}

val wordInfoViewModule = module {
    viewModel {
        WordInfoViewModel(get())
    }
}

val selectPlayersViewModel = module {
    viewModel {
        SelectPlayersViewModel()
    }
}
