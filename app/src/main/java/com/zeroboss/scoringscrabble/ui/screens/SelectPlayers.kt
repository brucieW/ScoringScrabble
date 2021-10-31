package com.zeroboss.scoringscrabble.ui.screens

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
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
import androidx.compose.ui.res.painterResource
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
        topBar = { SelectPlayersAppBar(navController) },
        content = { Body(navController, get()) },
        backgroundColor = Blue50
    )
}

@Composable
fun SelectPlayersAppBar(
    navController: NavController
) {
    Row(
        modifier = Modifier
            .height(58.dp)
            .background(Blue800),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painterResource(R.drawable.logo),
            "",
            modifier = Modifier
                .padding(start = 10.dp, end = 20.dp)
        )

        Text(
            text = stringResource(id = R.string.select_players),
            style = navigationTitle2,
            modifier = Modifier.weight(1f)
        )

        DropDownMenu(navController)

        IconButton(
            onClick = { Runtime.getRuntime().exit(0) }
        ) {
            Icon(
                Icons.Rounded.Close,
                contentDescription = "Close Application",
                tint = Color.White,
                modifier = Modifier.padding(end = 10.dp)
            )
        }
    }
}

@Composable
fun Body(
    navController: NavController,
    selectPlayersViewModel: SelectPlayersViewModel
) {
    val usingTeams: Boolean by selectPlayersViewModel.isTeamType
    val enabled: Boolean by selectPlayersViewModel.dataValid
    val uniqueNames: Boolean by selectPlayersViewModel.uniquePlayerNames

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

            PlayerSelection(
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
                        navController.navigate("score_sheet")
                    },

                    secondButtonText = "Cancel",
                    onSecondButtonClicked = { selectPlayersViewModel.clearAllPlayerNames() }
                )
            )
        }
    }
}

@Composable
fun TeamSelection(
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
fun PlayerSelection(
    usingTeams: Boolean,
    selectPlayersViewModel: SelectPlayersViewModel
) {
    val names = mutableListOf<String>()

    for (i in 0..3) {
        val name: String by selectPlayersViewModel.playerNames[i]
        names.add(name)
    }

    val uniqueNames: Boolean by selectPlayersViewModel.uniquePlayerNames

    val teamDropdownVisible = mutableListOf<Boolean>()

    for (teamId in 0..1) {
        val isVisible: Boolean by selectPlayersViewModel.teamListVisible[teamId]
        teamDropdownVisible.add(isVisible)
    }

    val playerDropDownsVisible = mutableListOf<Boolean>()

    for (playerId in 0..3) {
        val isVisible: Boolean by selectPlayersViewModel.playerListVisible[playerId]
        playerDropDownsVisible.add(isVisible)
    }

    for (teamId in 0..1) {
        PlayerNames(
            usingTeams,
            teamId,
            names,
            teamDropdownVisible,
            playerDropDownsVisible,
            selectPlayersViewModel
        )
    }
}

@Composable
fun PlayerNames(
    usingTeams: Boolean,
    teamId: Int,
    names: List<String>,
    teamVisibleState: List<Boolean>,
    dropDownState: List<Boolean>,
    selectPlayersViewModel: SelectPlayersViewModel
) {
    if (usingTeams) {
        TextWithDropDown(
            text = "Team ${teamId + 1}",
            modifier = Modifier.padding(top = 5.dp),
            horizontal = Arrangement.Start,
            onClickedDropDown = { selectPlayersViewModel.onTeamDropdownClicked(teamId) },
            onFocusAltered = { selectPlayersViewModel.setTeamDropdownVisible(teamId, false)},
        )

        TeamSelection(
            teamVisibleState[teamId],
            selectPlayersViewModel,
            teamId
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

            if (dropDownState[playerId]) {
                PlayerNamesSelection(
                    selectPlayersViewModel,
                    playerId
                )
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
                        text = AnnotatedString(name),
                        style = normalText,
                        onClick = {
                            closePlayerPopup(selectPlayersViewModel, playerId)
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
