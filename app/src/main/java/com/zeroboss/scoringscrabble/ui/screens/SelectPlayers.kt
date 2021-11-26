package com.zeroboss.scoringscrabble.ui.screens

import androidx.compose.animation.ExperimentalAnimationApi
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
import androidx.compose.runtime.livedata.observeAsState
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
import com.zeroboss.scoring500.ui.common.TextWithDropDown
import com.zeroboss.scoringscrabble.ui.viewmodels.SelectPlayersViewModel
import com.zeroboss.scoringscrabble.R
import com.zeroboss.scoringscrabble.ui.common.*
import com.zeroboss.scoringscrabble.ui.theme.*
import org.koin.androidx.compose.get

@ExperimentalAnimationApi
@ExperimentalMaterialApi
@Composable
fun SelectPlayers(
    navController: NavController
) {
    val scaffoldState = rememberScaffoldState()

    Scaffold(
        scaffoldState = scaffoldState,
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopPanel(
                R.string.select_players,
                onClickReturn = {
                    navController.popBackStack()
                }
            )
        },

        content = {
            SelectPlayersBody(
                navController,
                get()
            )
        }
    )
}

@Composable
fun SelectPlayersBody(
    navController: NavController,
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
                        navController.popBackStack()
                        navController.navigate("score_sheet")
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
fun TeamNameSelection(
    isVisible: Boolean = false,
    selectPlayersViewModel: SelectPlayersViewModel,
    teamId: Int
) {
    if (isVisible) {
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
                                    selectPlayersViewModel.setPlayerNames(
                                        teamId,
                                        players
                                    )
                                }
                            )
                        }
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

@Composable
fun PlayerNameSelection(
    usingTeams: Boolean,
    selectPlayersViewModel: SelectPlayersViewModel
) {
    // Setup Observers
    val teamDropDownsVisible = mutableListOf<State<Boolean>>(
        selectPlayersViewModel.isTeamListVisible(0).observeAsState(false),
        selectPlayersViewModel.isTeamListVisible(1).observeAsState(false),
    )

    val names = mutableListOf<State<String>>()
    val playerDropDownsVisible = mutableListOf<State<Boolean>>()

    for (playerId in 0..3) {
        names.add(selectPlayersViewModel.playerNames[playerId].observeAsState(""))
        playerDropDownsVisible.add(selectPlayersViewModel.playerListVisible[playerId].observeAsState(false))
    }

    for (teamId in 0..1) {
        if (usingTeams) {
            TextWithDropDown(
                text = "Team ${teamId + 1}",
                modifier = Modifier.padding(top = 5.dp),
                horizontal = Arrangement.Start,
                onClickedDropDown = { selectPlayersViewModel.onTeamDropdownClicked(teamId) },
                onFocusAltered = { selectPlayersViewModel.setTeamDropdownVisible(teamId, false) }
            )
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
                        .padding(bottom = 5.dp, end = 5.dp)
                        .weight(0.5F),
                    text = names[playerId].value,
                    label = "Player ${playerId + 1}",
                    onNameChanged = { text ->
                        selectPlayersViewModel.onPlayerNameChanged(
                            1,
                            playerId,
                            text
                        )
                    },
                    onTrailingIconClicked = {
                        selectPlayersViewModel.onPlayerDropdownClicked(1, playerId)
                    },
                    onFocusAltered = { selectPlayersViewModel.clearAllPlayerLists() }
                )

                if (playerDropDownsVisible[playerId].value) {
                    PlayerNamesSelection(
                        selectPlayersViewModel,
                        teamId,
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
                            closePlayerPopup(
                                selectPlayersViewModel,
                                teamId,
                                playerId)

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
