package com.zeroboss.scoringscrabble.ui.screens.scoresheet

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.*
import androidx.navigation.NavController
import com.zeroboss.scoringscrabble.R
import com.zeroboss.scoringscrabble.data.common.ActiveStatus
import com.zeroboss.scoringscrabble.ui.common.TopPanel
import com.zeroboss.scoringscrabble.ui.theme.*
import com.zeroboss.scoringscrabble.ui.viewmodels.ScoringSheetViewModel
import org.koin.androidx.compose.get

@Composable
fun ScoreSheet(
    navController: NavController,
    scoringViewModel: ScoringSheetViewModel
) {
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
                onClickReturn = { navController.popBackStack() }
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

