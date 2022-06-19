package com.zeroboss.scoringscrabble.presentation.screens.scoresheet

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.*
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.zeroboss.scoringscrabble.R
import com.zeroboss.scoringscrabble.data.common.ActiveStatus
import com.zeroboss.scoringscrabble.presentation.common.TopPanel
import com.zeroboss.scoringscrabble.ui.theme.*
import org.koin.androidx.compose.viewModel

@Destination
@Composable
fun ScoreSheet(
    navigator: DestinationsNavigator
) {
    val scoringViewModel by viewModel<ScoringSheetViewModel>()
    val scaffoldState = rememberScaffoldState()
    val colState = rememberLazyListState()
    val rowState = rememberLazyListState()
    val firstPlayerSelected by scoringViewModel.firstPlayerSelected

    Scaffold(
        scaffoldState = scaffoldState,
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopPanel(
                R.string.score_sheet,
                onClickReturn = {  navigator.popBackStack() }
            )
        },
        content = {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                state = colState
            ) {
                if (!firstPlayerSelected) {
                    item {
                        Text(
                            text = stringResource(id = if (ActiveStatus.activeMatch!!.isTeamType()) R.string.first_team else R.string.first_player),
                            style = errorText,
                            modifier = Modifier.padding(start = 60.dp, top = 20.dp)
                        )
                    }
                }

                item {
                    LazyRow(
                        modifier = Modifier.fillMaxHeight(),
                        state = rowState
                    ) {
                        item {
                            TurnsColumn()
                        }

                        if (ActiveStatus.activeMatch!!.isTeamType()) {
                            itemsIndexed(
                                items = scoringViewModel.teams,
                                itemContent = { index, team ->
                                    PlayerTeamCard(
                                        scoringViewModel,
                                        index,
                                        team = team
                                    )
                                })
                        } else {
                            itemsIndexed(
                                items = scoringViewModel.players,
                                itemContent = { index, player ->
                                    PlayerTeamCard(
                                        scoringViewModel,
                                        index,
                                        player = player,
                                    )
                                })
                        }

                        item {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                BoardHeader(scoringViewModel)
                                ScrabbleBoard(scoringViewModel)
                            }
                        }
                    }
                }
            }
        }
    )
}
