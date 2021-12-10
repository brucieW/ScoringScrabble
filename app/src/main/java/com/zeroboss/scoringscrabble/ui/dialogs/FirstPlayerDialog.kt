package com.zeroboss.scoringscrabble.ui.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Checkbox
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.zeroboss.scoringscrabble.R
import com.zeroboss.scoringscrabble.data.common.ActiveStatus
import com.zeroboss.scoringscrabble.ui.common.MultipleButtonBar
import com.zeroboss.scoringscrabble.ui.common.getTwoButtons
import com.zeroboss.scoringscrabble.ui.theme.*
import com.zeroboss.scoringscrabble.ui.viewmodels.ScoringSheetViewModel

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun FirstPlayerDialog(
    scoringSheetViewModel: ScoringSheetViewModel,
    showDialog: Boolean,
    setShowDialog: (Boolean) -> Unit
) {
    if (showDialog) {
        Dialog(
            onDismissRequest = { setShowDialog(false) }
        ) {
            Card(
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(20.dp)
            ) {
                val listState = rememberLazyListState()
                var selectedIndex by remember { mutableStateOf(-1) }

                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    if (ActiveStatus.isTeamGame) {
                        item {
                            Text(
                                text = stringResource(id = R.string.first_team),
                                style = dialogTitle
                            )
                        }

                        itemsIndexed(items = scoringSheetViewModel.teams) { index, team ->
                            Text(
                                text = team.getTeamName(),
                                style = typography.h5,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(if (index == selectedIndex) Color.DarkGray else Blue50)
                                    .selectable(
                                        selected = index == selectedIndex,
                                        onClick = {
                                            if (selectedIndex != index)
                                                selectedIndex = index else selectedIndex = -1
                                        }
                                    )
                            )
                        }
                    } else {
                        item {
                            Text(
                                text = stringResource(id = R.string.first_player),
                                style = dialogTitle
                            )
                        }

                        itemsIndexed(items = scoringSheetViewModel.players) { index, player ->
                            Text(
                                text = player.name,
                                style = typography.h5,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(if (index == selectedIndex) Color.DarkGray else Blue50)
                                    .selectable(
                                        selected = index == selectedIndex,
                                        onClick = {
                                            if (selectedIndex != index)
                                                selectedIndex = index else selectedIndex = -1
                                        }
                                    )
                            )
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(20.dp))

                        MultipleButtonBar(
                            getTwoButtons(
                                firstButtonEnabled = selectedIndex != -1,
                                onFirstButtonClicked = {
                                    scoringSheetViewModel.firstPlayer = selectedIndex
                                    setShowDialog(false)
                                },
                                onSecondButtonClicked = {
                                    setShowDialog(false)
                                }
                            )
                        )
                    }
                }
            }
        }
    }
}


