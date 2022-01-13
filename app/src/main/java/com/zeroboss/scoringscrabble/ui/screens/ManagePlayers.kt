package com.zeroboss.scoringscrabble.ui.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.zeroboss.scoringscrabble.R
import com.zeroboss.scoringscrabble.ui.common.TopPanel
import com.zeroboss.scoringscrabble.ui.viewmodels.HomeViewModel
import com.zeroboss.scoringscrabble.ui.viewmodels.ScoringSheetViewModel
import org.koin.androidx.compose.get
import org.koin.androidx.compose.viewModel

@Destination
@Composable
fun ManagePlayers(
    navigator: DestinationsNavigator
) {
    val scaffoldState = rememberScaffoldState()

    Scaffold(
        scaffoldState = scaffoldState,
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopPanel(
                R.string.select_players,
                onClickReturn = {
                    navigator.popBackStack()
                }
            )
        },

        content = {
            ManagePlayersBody()
        }
    )
}

@Composable
fun ManagePlayersBody() {
    val homeViewModel by viewModel<HomeViewModel>()
}