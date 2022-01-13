package com.zeroboss.scoringscrabble.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.navigation.NavController
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.zeroboss.scoring500.ui.common.TextWithDropDown
import com.zeroboss.scoringscrabble.ui.viewmodels.SelectPlayersViewModel
import com.zeroboss.scoringscrabble.R
import com.zeroboss.scoringscrabble.ui.common.*
import com.zeroboss.scoringscrabble.ui.screens.destinations.ScoreSheetDestination
import com.zeroboss.scoringscrabble.ui.theme.*
import org.koin.androidx.compose.get

@Destination
@Composable
fun SelectPlayers(
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
            SelectPlayersBody(
                navigator,
                get()
            )
        }
    )
}

@Composable
fun SelectPlayersBody(
    navigator: DestinationsNavigator,
    selectPlayersViewModel: SelectPlayersViewModel
) {
    val usingTeams by selectPlayersViewModel.isTeamType
    val enabled by selectPlayersViewModel.dataValid
    val uniqueNames by selectPlayersViewModel.uniquePlayerNames

    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
        shape = RoundedCornerShape(20.dp),
        elevation = 10.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {
            CommonCheckBox(
                text = R.string.playing_with_teams,
                checked = usingTeams,
                checkStateChanged = { selectPlayersViewModel.changeUsingTeams() },
                modifier = Modifier.padding(end = 30.dp),
                horizontalArrangement = Arrangement.Center
            )

            PlayerNameSelection(
                usingTeams,
                selectPlayersViewModel
            )

            if (!uniqueNames) {
                Text(
                    modifier = Modifier
                        .padding(top = 5.dp)
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    text = stringResource(id = R.string.unique_names),
                    color = Color.Red,
                    style = normalText,
                    fontStyle = FontStyle.Italic
                )
            }

            MultipleButtonBar(
                modifier = Modifier.padding(20.dp),
                buttonData = getTwoButtons(
                    firstButtonText = "Start Scoring",
                    firstButtonEnabled = enabled && uniqueNames,
                    onFirstButtonClicked = {
                        selectPlayersViewModel.onStartScoring()
                        navigator.navigate(ScoreSheetDestination())
                    },

                    secondButtonText = "Cancel",
                    onSecondButtonClicked = {
                        selectPlayersViewModel.clearPlayerNames()
                    }
                )
            )
        }
    }
}

@Composable
fun PlayerNameSelection(
    usingTeams: Boolean,
    selectPlayersViewModel: SelectPlayersViewModel
) {
    // Setup Observers
    val teamDropDownsVisible = mutableListOf<Boolean>()

    for (i in 0..1) {
        val listVisible by selectPlayersViewModel.teamListVisible[i]
        teamDropDownsVisible.add(listVisible)
    }

    val names = mutableListOf<String>()
    val playerDropDownsVisible = mutableListOf<Boolean>()

    for (playerId in 0..3) {
        val name by selectPlayersViewModel.playerNames[playerId]
        names.add(name)

        val visible by selectPlayersViewModel.playerListVisible[playerId]
        playerDropDownsVisible.add(visible)
    }

    for (teamId in 0..1) {
        if (usingTeams) {
            TextWithDropDown(
                text = "Team ${teamId + 1}",
                horizontal = Arrangement.Start,
                onClickedDropDown = { selectPlayersViewModel.onTeamDropdownClicked(teamId) },
                onFocusAltered = { selectPlayersViewModel.setTeamDropdownVisible(teamId, false) }
            )

            if (teamDropDownsVisible[teamId]) {
                TeamNameSelection(selectPlayersViewModel, teamId)
            }
        }

        val start = if (teamId == 0) 0 else 2
        val end = if (teamId == 0) 1 else 3

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            for (playerId in start..end) {
                TextFieldWithDropDown(
                    modifier = Modifier
                        .padding(end = 10.dp)
                        .weight(0.5F),
                    text = names[playerId],
                    label = "Player ${playerId + 1}",
                    onNameChanged = { text ->
                        selectPlayersViewModel.onPlayerNameChanged(
                            playerId,
                            text
                        )
                    },
                    onTrailingIconClicked = {
                        selectPlayersViewModel.onPlayerDropdownClicked(playerId)
                    },
                    onFocusAltered = { selectPlayersViewModel.clearAllPlayerLists() }
                )

                if (playerDropDownsVisible[playerId]) {
                    PlayerNamesSelection(
                        selectPlayersViewModel,
                        playerId
                    )
                }
            }
        }
    }
}

@Composable
fun PlayerNamesSelection(
    selectPlayersViewModel: SelectPlayersViewModel,
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
                    onClick = { closePlayerPopup(selectPlayersViewModel, playerId) }
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
                        modifier = Modifier.fillMaxWidth(),
                        text = AnnotatedString(name),
                        style = normalText,
                        onClick = {
                            closePlayerPopup(
                                selectPlayersViewModel,
                                playerId
                            )

                            selectPlayersViewModel.onPlayerNameChanged(
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
    playerId: Int
) {
    selectPlayersViewModel.setPlayerDropdownVisible(
        playerId,
        false
    )
}

@Composable
fun TeamNameSelection(
    selectPlayersViewModel: SelectPlayersViewModel,
    teamId: Int
) {
    Popup() {
        Box(
            modifier = Modifier
                .size(180.dp, 300.dp)
                .background(
                    Color.LightGray,
                    RoundedCornerShape(10.dp)
                )

        ) {
            Column(
            ) {
                Row(
                    modifier = Modifier
                        .padding(start = 10.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Select Team",
                        style = normalText,
                        color = Blue800
                    )

                    IconButton(
                        onClick = { closeTeamPopup(selectPlayersViewModel, teamId) }
                    ) {
                        Icon(
                            Icons.Rounded.Close, contentDescription = "",
                            tint = Blue800
                        )
                    }
                }

                LazyColumn(
                    modifier = Modifier
                        .background(
                            Color.LightGray,
                            RoundedCornerShape(10.dp)
                        )
                        .padding(start = 10.dp, end = 10.dp, bottom = 10.dp, top = 0.dp)
                ) {
                    itemsIndexed(selectPlayersViewModel.getFilteredTeams()) { _, name ->
                        ClickableText(
                            text = AnnotatedString(name),
                            style = normalText,
                            onClick = {
                                selectPlayersViewModel.setTeamDropdownVisible(
                                    teamId,
                                    false
                                )

                                val players = name.split("/")
                                selectPlayersViewModel.setTeamPlayerNames(teamId, players)
                            }
                        )
                    }
                }
            }
        }
    }
}


private fun closeTeamPopup(
    selectPlayersViewModel: SelectPlayersViewModel,
    teamId: Int
) {
    selectPlayersViewModel.setTeamDropdownVisible(
        teamId,
        false
    )
}


