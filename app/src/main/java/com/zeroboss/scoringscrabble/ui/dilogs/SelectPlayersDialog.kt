package com.zeroboss.scoringscrabble.ui.dilogs

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.Popup
import com.zeroboss.scoringscrabble.ui.common.ShowDialogText
import com.zeroboss.scoringscrabble.ui.theme.typography
import com.zeroboss.scoringscrabble.ui.viewmodels.SelectPlayersViewModel
import com.zeroboss.scoringscrabble.R
import com.zeroboss.scoringscrabble.ui.common.CommonCheckBox
import com.zeroboss.scoringscrabble.ui.common.ScoringButton
import com.zeroboss.scoringscrabble.ui.common.TextFieldWithDropDown
import com.zeroboss.scoringscrabble.ui.theme.Blue800
import com.zeroboss.scoringscrabble.ui.theme.normalText

@Composable
fun SelectPlayersDialog(
    showDialog: Boolean,
    setShowDialog: (Boolean) -> Unit,
    selectPlayersViewModel: SelectPlayersViewModel
) {
    if (showDialog) {
        val usingTeams by selectPlayersViewModel.isTeamType.observeAsState()

        val enabled by selectPlayersViewModel.dataValid.observeAsState()
        val uniqueNames by selectPlayersViewModel.uniquePlayerNames.observeAsState()

        Dialog(
            onDismissRequest = { setShowDialog(false) }
        ) {
            Card(
                modifier = Modifier.size(340.dp, 440.dp),
                shape = RoundedCornerShape(20.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(R.drawable.logo),
                            contentDescription = "",
                            modifier = Modifier.fillMaxWidth(fraction = .35F)
                        )

                        ShowDialogText(
                            style = typography.h5,
                            topPadding = 0.dp,
                            text = R.string.select_players
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    CommonCheckBox(
                        text = R.string.playing_with_teams,
                        checked = usingTeams!!,
                        checkStateChanged = { selectPlayersViewModel.changeUsingTeams() }
                    )

                    PlayerSelection(
                        usingTeams!!,
                        selectPlayersViewModel)

//                    TeamSelection(
//                        usingTeams,
//                        selectPlayersViewModel
//                    )

                    ScoringButton(
                        enabled = !enabled!!,
                        text = " OK ",
                        modifier = Modifier.padding(top = 20.dp),
                        onClick = {}
                    )
                }
            }
        }
    }
}

@Composable
fun PlayerSelection(
    usingTeams: Boolean,
    selectPlayersViewModel: SelectPlayersViewModel
) {
    if (!usingTeams) {
        val names = mutableListOf<State<String?>>()
        val playerDropDownsVisible = mutableListOf<State<Boolean?>>()

        for (playerId in 0..3) {
            names.add(selectPlayersViewModel.getPlayerNameWithOffset(playerId).observeAsState())
            playerDropDownsVisible.add(
                selectPlayersViewModel.isPlayerDropDownVisibleWithOffset(playerId).observeAsState())

            TextFieldWithDropDown(
                text = names[playerId].value!!,
                label = "Player ${playerId + 1}",
                onNameChanged = { text ->
                    selectPlayersViewModel.onPlayerNameChanged(
                        -1,
                        playerId,
                        text
                    )
                },
                onTrailingIconClicked = { selectPlayersViewModel.onPlayerDropdownClicked(-1, playerId) },
                onFocusAltered = { selectPlayersViewModel.clearAllPlayerLists() }
            )

            if (playerDropDownsVisible[selectPlayersViewModel.getOffset(-1, playerId)].value!!) {
                PlayerNamesSelection(
                    selectPlayersViewModel,
                    -1,
                    playerId
                )
            }
        }

    }
}

@Composable
fun PlayerNamesSelection(
    selectPlayersViewModel: SelectPlayersViewModel,
    teamId: Int,
    playerId: Int
) {
    Popup() {
        Column(
            modifier = Modifier
                .background(
                    Color.LightGray,
                    RoundedCornerShape(10.dp)
                )
        ) {
            Row(
                modifier = Modifier.padding(start = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Select Player",
                    style = normalText,
                    color = Blue800
                )

                IconButton(
                    onClick = { closePlayerPopup(selectPlayersViewModel, teamId, playerId) }
                ) {
                    Icon(
                        Icons.Rounded.Close, contentDescription = "",
                        tint = Blue800
                    )
                }
            }

            LazyColumn(
                modifier = Modifier
                    .size(160.dp, 300.dp)
                    .background(
                        Color.LightGray,
                        RoundedCornerShape(10.dp)
                    )
                    .padding(start = 10.dp, end = 10.dp, bottom = 10.dp, top = 0.dp)
            ) {
                itemsIndexed(selectPlayersViewModel.getFilteredPlayers()) { _, name ->
                    ClickableText(
                        text = AnnotatedString(name),
                        style = normalText,
                        onClick = {
                            closePlayerPopup(selectPlayersViewModel, teamId, playerId)
                            selectPlayersViewModel.onPlayerNameChanged(
                                teamId,
                                playerId,
                                name
                            )
                        }
                    )
                }
            }
        }
    }
}

private fun closePlayerPopup(
    selectPlayersViewModel: SelectPlayersViewModel,
    teamId: Int,
    playerId: Int
) {
    selectPlayersViewModel.setPlayerDropdownVisible(
        teamId,
        playerId,
        false
    )
}

@Composable
fun TeamSelection(
    usingTeams: Boolean,
    selectPlayersViewModel: SelectPlayersViewModel
) {
    if (usingTeams) {

    }
}
