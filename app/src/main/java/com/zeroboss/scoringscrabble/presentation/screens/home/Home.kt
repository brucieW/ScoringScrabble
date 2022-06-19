package com.zeroboss.scoringscrabble.presentation.screens.home

import android.annotation.SuppressLint
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.rounded.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.zeroboss.scoringscrabble.presentation.dialogs.ClearBackupsDialog
import com.zeroboss.scoringscrabble.presentation.dialogs.ClearDataDialog
import com.zeroboss.scoringscrabble.presentation.dialogs.RestoreDataDialog
import com.zeroboss.scoringscrabble.R
import com.zeroboss.scoringscrabble.data.common.ActiveStatus
import com.zeroboss.scoringscrabble.data.common.CommonDb
import com.zeroboss.scoringscrabble.data.common.CommonDb.createGame
import com.zeroboss.scoringscrabble.data.common.EXPAND_ANIMATION_DURATION
import com.zeroboss.scoringscrabble.data.entities.Game
import com.zeroboss.scoringscrabble.data.entities.Match
import com.zeroboss.scoringscrabble.di.homeViewModelModule
import com.zeroboss.scoringscrabble.presentation.common.*
import com.zeroboss.scoringscrabble.presentation.common.menu.DropdownMenuExt
import com.zeroboss.scoringscrabble.presentation.common.menu.DropdownMenuItemExt
import com.zeroboss.scoringscrabble.presentation.dialogs.AboutDialog
import com.zeroboss.scoringscrabble.presentation.dialogs.BackupDataDialog
import com.zeroboss.scoringscrabble.presentation.dialogs.DeleteGameDialog
import com.zeroboss.scoringscrabble.presentation.dialogs.DeleteMatchDialog
import com.zeroboss.scoringscrabble.presentation.screens.destinations.ManagePlayersDestination
import com.zeroboss.scoringscrabble.presentation.screens.destinations.ScoreSheetDestination
import com.zeroboss.scoringscrabble.presentation.screens.destinations.SelectPlayersDestination
import com.zeroboss.scoringscrabble.presentation.screens.destinations.StatisticsDestination
import com.zeroboss.scoringscrabble.ui.theme.*
import org.koin.androidx.compose.get
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@Destination
@Composable
fun Home(
    navigator: DestinationsNavigator
) {
    val scaffoldState = rememberScaffoldState()

    Scaffold(
        scaffoldState = scaffoldState,
        modifier = Modifier.fillMaxSize(),
        topBar = { HomeAppBar(navigator) },
        content = { Body(navigator, get()) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navigator.navigate(SelectPlayersDestination())
                },
                contentColor = Color.White,
                backgroundColor = Blue800,
            ) {
                Icon(Icons.Filled.Add, "")
            }
        },
        isFloatingActionButtonDocked = true,
        floatingActionButtonPosition = FabPosition.End,
    )
}

@Composable
fun HomeAppBar(
    navigator: DestinationsNavigator
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
            text = stringResource(id = R.string.app_name),
            style = navigationTitle2,
            modifier = Modifier.weight(1f)
        )

        DropDownMenu(navigator)

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

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Body(
    navigator: DestinationsNavigator,
    homeViewModel: HomeViewModel
) {
    val matches = homeViewModel.matches.collectAsState()
    val expandedCard = remember { homeViewModel.expandedCard }

    val (showDeleteMatch, setShowDeleteMatch) = remember { mutableStateOf(false) }

    DeleteMatchDialog(
        showDialog = showDeleteMatch,
        setShowDialog = setShowDeleteMatch,
        onYesClicked = {
            homeViewModel.deleteActiveMatch()
        }
    )

    val (showDeleteGame, setShowDeleteGame) = remember { mutableStateOf(false) }

    DeleteGameDialog(
        showDialog = showDeleteGame,
        setShowDialog = setShowDeleteGame,
        onYesClicked = {
            homeViewModel.deleteActiveGame()
        },
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Blue50)
            .padding(top = 10.dp, bottom = 10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Match History",
                style = typography.h5
            )
        }

        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            if (matches.value.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "There are no Match records. Click the button below to create a new match.",
                        textAlign = TextAlign.Center,
                        style = typography.h5,
                        modifier = Modifier.padding(start = 40.dp, end = 40.dp, bottom = 40.dp)
                    )

                    Box(
                        Modifier
                            .rotate(20F)
                            .padding(start = 50.dp, end = 100.dp, top = 40.dp)
                    ) {
                        Image(
                            painterResource(id = R.drawable.arrow), contentDescription = null
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    itemsIndexed(matches.value) { index, matchCard ->
                        ExpandableMatchCard(
                            matchCard,
                            onCardArrowClicked = {
                                homeViewModel.onCardArrowClicked(matchCard)
                            },

                            onNewGameClicked = {
                                ActiveStatus.activeMatch = matchCard
                                createGame(matchCard)
                                navigator.navigate(
                                    ScoreSheetDestination()
                                )
                            },

                            onDeleteMatchClicked = {
                                ActiveStatus.activeMatch = matchCard
                                setShowDeleteMatch(true)
                            },

                            onGameClicked = { game ->
                                ActiveStatus.activeMatch = matchCard
                                ActiveStatus.activeGame = game

                                if (game.playerTurnData.isEmpty()) {
                                    navigator.navigate(SelectPlayersDestination())
                                } else {
                                    navigator.navigate(ScoreSheetDestination())
                                }
                            },

                            onEditGamesClicked = {
                                ActiveStatus.activeMatch = matchCard
                                navigator.navigate(ScoreSheetDestination())
                            },

                            onDeleteGameClicked = {
                                ActiveStatus.activeMatch = matchCard
                                ActiveStatus.activeGame = it
                                setShowDeleteGame(true)
                            },

                            expanded = expandedCard.value == matchCard
                        )
                    }
                }
            }
        }
    }
}

@ExperimentalMaterialApi
@SuppressLint("UnusedTransitionTargetStateParameter")
@Composable
fun ExpandableMatchCard(
    match: Match,
    onCardArrowClicked: () -> Unit,
    onNewGameClicked: () -> Unit,
    onEditGamesClicked: () -> Unit,
    onDeleteMatchClicked: () -> Unit,
    onGameClicked: (game: Game) -> Unit,
    onDeleteGameClicked: (game: Game) -> Unit,
    expanded: Boolean
) {
    val transitionState = remember {
        MutableTransitionState(expanded).apply {
            targetState = !expanded
        }
    }

    val transition = updateTransition(transitionState, label = "transition")

    val arrowRotationDegree by transition.animateFloat({
        tween(durationMillis = EXPAND_ANIMATION_DURATION)
    }, label = "rotationDegreeTransition") {
        if (expanded) 0F else 180f
    }

    Card(
        onClick = { onCardArrowClicked() },
        elevation = 10.dp,
    ) {
        Column(
            modifier = Modifier
                .padding(top = 10.dp, bottom = 10.dp)
                .animateContentSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                CardArrow(
                    degrees = arrowRotationDegree,
                    onClick = onCardArrowClicked
                )

                Column() {
                    Text(
                        text = if (match.isTeamType()) "Teams:" else "Players:",
                        style = dialogTitle
                    )

                    Text(
                        text = match.lastPlayed.format(
                            DateTimeFormatter.ofLocalizedDate(
                                FormatStyle.MEDIUM
                            )
                        ),
                        style = smallerText,
                        fontStyle = FontStyle.Italic
                    )
                }

                Column() {
                    if (match.isTeamType()) {
                        match.teams.forEach { team ->
                            Text(
                                text = team.getTeamName(),
                                style = normalText
                            )
                        }
                    } else {
                        match.players.forEach { player ->
                            Text(
                                text = player.name,
                                style = normalText
                            )
                        }
                    }
                }

//                Column(
//                    horizontalAlignment = Alignment.CenterHorizontally,
//                    verticalArrangement = Arrangement.Center,
//                    modifier = Modifier.weight(0.33F)
//                ) {
//                    Row() {
//                        Text(
//                            text = "WinLoss", //match.getWinLossRatioText(),
//                            style = normalText,
//                            modifier = Modifier.padding(end = 5.dp)
//                        )
//
////                        if (match.gameIsActive()) {
////                            Image(
////                                painterResource(id = R.drawable.star),
////                                contentDescription = "",
////                                modifier = Modifier.size(20.dp)
////                            )
////                        }
//                    }

                DeleteIcon(
                    onClick = onDeleteMatchClicked
                )
            }

            if (expanded) {
                Row(
                    modifier = Modifier.padding(top = 5.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    ScoringButton(
                        text = stringResource(id = R.string.new_game),
                        onClick = onNewGameClicked,
                        smallButtons = true,
                        modifier = Modifier.padding(end = 20.dp)
                    )

                    if (match.games.isNotEmpty()) {
                        ScoringButton(
                            text = stringResource(id = R.string.edit_games),
                            onClick = onEditGamesClicked,
                            smallButtons = true,
                            modifier = Modifier.padding(bottom = 10.dp)
                        )
                    }
                }

                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    val openGames = match.games.filter { it.finished == null }
                        .sortedByDescending { it.started }
                    val finishedGames = match.games.filter { it.finished != null }
                        .sortedByDescending { it.started }
                    val games = openGames + finishedGames

                    games.forEach { game ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 5.dp, start = 10.dp, end = 10.dp)
                                .clickable {
                                    homeViewModelModule
                                    onGameClicked(game)
                                },
                            elevation = 5.dp
                        ) {
                            Column(
//                                modifier = Modifier.weight(.9f)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(5.dp),
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    if (game.finished == null) {
                                        Image(
                                            painterResource(id = R.drawable.star),
                                            contentDescription = "",
                                            modifier = Modifier
                                                .size(25.dp)
                                                .padding(end = 5.dp)
                                        )

                                        Text(
                                            buildAnnotatedString {
                                                withStyle(style = SpanStyle(fontSize = 16.sp)) {
                                                    append("-9") //append(game.displayTotalScores())
                                                    append("  ")
                                                }
                                                withStyle(
                                                    style = SpanStyle(
                                                        fontSize = 12.sp,
                                                        fontStyle = FontStyle.Italic
                                                    )
                                                ) {
                                                    append(
                                                        game.started.format(
                                                            DateTimeFormatter.ofLocalizedDateTime(
                                                                FormatStyle.SHORT,
                                                                FormatStyle.SHORT
                                                            )
                                                        )
                                                    )
                                                }
                                            },

                                            textAlign = TextAlign.Center,
                                        )
                                    } else {
//                                        val teamId = 1 //game.getWinningTeamId()
//                                        val losingTeamId = if (teamId == 0) 1 else 0
//                                        val scores = game.getHandsTotal()
//
//                                        Text(
//                                            buildAnnotatedString {
//                                                withStyle(
//                                                    style = SpanStyle(fontSize = 16.sp)
//                                                ) {
//                                                    append(game.match.target.teams[teamId].name)
//                                                    append(" won ")
//                                                    append(scores.last()[teamId].toString())
//                                                    append(" to ")
//                                                    append(scores.last()[losingTeamId].toString())
//                                                    append(" ")
//                                                }
//
//                                                withStyle(
//                                                    style = SpanStyle(
//                                                        fontSize = 12.sp,
//                                                        fontStyle = FontStyle.Italic
//                                                    )
//                                                ) {
//                                                    append(
//                                                        game.finished!!.format(
//                                                            DateTimeFormatter.ofLocalizedDateTime(
//                                                                FormatStyle.SHORT,
//                                                                FormatStyle.SHORT
//                                                            )
//                                                        )
//                                                    )
//                                                }
//                                            }
//                                        )
                                    }
                                }
                            }

                            Column(
//                                modifier = Modifier
//                                    .weight(0.1f),
                                horizontalAlignment = Alignment.End,
                                verticalArrangement = Arrangement.Center
                            ) {
                                DeleteIcon(
                                    onClick = { onDeleteGameClicked(game) },
                                    modifier = Modifier.size(30.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CardArrow(
    degrees: Float,
    onClick: () -> Unit
) {
    IconButton(
        onClick = onClick,
        content = {
            Icon(
                painter = painterResource(id = R.drawable.ic_expand_less_24),
                contentDescription = "Expandable Arrow",
                modifier = Modifier.rotate(degrees),
            )
        }
    )
}

@Composable
fun DropDownMenu(
    navigator: DestinationsNavigator
) {
    var expanded by remember { mutableStateOf(false) }
    val (showAbout, setShowAbout) = remember { mutableStateOf(false) }
    val (showPreferences, setShowPreferences) = remember { mutableStateOf(false) }
    val (showBackupDialog, setShowBackupDialog) = remember { mutableStateOf(false) }
    val (showRestoreDialog, setShowRestoreDialog) = remember { mutableStateOf(false) }
    val (showClearBackupsDialog, setShowClearBackupsDialog) = remember { mutableStateOf(false) }
    val (showClearDataDialog, setShowClearDataDialog) = remember { mutableStateOf(false) }

    Column() {
        Image(
            painterResource(R.drawable.menu), "",
            modifier = Modifier
                .size(48.dp)
                .clickable { expanded = true }
        )

        MaterialTheme(colors = menuColors) {
            DropdownMenuExt(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.padding(end = 10.dp)
            ) {
                DropdownMenuItemExt(
                    onClick = {
                        expanded = false
                        navigator.navigate(ManagePlayersDestination())
                    }
                ) {
                    Text("Fix") //stringResource(R.string.manage_players))
                }

                DropdownMenuItemExt(
                    onClick = {
                        expanded = false
                        setShowPreferences(true)
                    }
                ) {
                    Text(stringResource(R.string.preferences))
                }

                DropdownMenuItemExt(
                    onClick = {
                        expanded = false
                        navigator.navigate(StatisticsDestination())
                    }
                ) {
                    Text(stringResource(R.string.statistics))
                }

                Divider()

                DropdownMenuItemExt(
                    onClick = {
                        expanded = false
                        setShowBackupDialog(true)
                    }
                ) {
                    Text(stringResource(R.string.backup_database))
                }

                DropdownMenuItemExt(
                    onClick = {
                        expanded = false
                        setShowRestoreDialog(true)
                    }
                ) {
                    Text(stringResource(R.string.restore_database))
                }

                if (CommonDb.getBackupFiles().isNotEmpty()) {
                    DropdownMenuItemExt(
                        onClick = {
                            expanded = false
                            setShowClearBackupsDialog(true)
                        }
                    ) {
                        Text(stringResource(R.string.clear_backups))
                    }
                }

                DropdownMenuItemExt(
                    onClick = {
                        expanded = false
                        setShowClearDataDialog(true)
                    }
                ) {
                    Text(stringResource(R.string.clear_database))
                }

                Divider()

                DropdownMenuItemExt(
                    onClick = {
                        expanded = false
                        setShowAbout(true)
                    }
                ) {
                    Text(stringResource(R.string.about))
                }
            }

            BackupDataDialog(
                showBackupDialog = showBackupDialog,
                setShowBackupDialog = setShowBackupDialog
            )

            RestoreDataDialog(
                showRestoreDialog = showRestoreDialog,
                setShowRestoreDialog = setShowRestoreDialog
            )

            ClearBackupsDialog(
                showClearBackupsDialog = showClearBackupsDialog,
                setShowClearBackups = setShowClearBackupsDialog,
            )

            ClearDataDialog(
                showClearData = showClearDataDialog,
                setShowClearData = setShowClearDataDialog
            )

            AboutDialog(
                showAbout = showAbout,
                setShowAbout = setShowAbout
            )
        }
    }
}

