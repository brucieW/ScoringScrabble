package com.zeroboss.scoringscrabble.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.zeroboss.scoringscrabble.R
import com.zeroboss.scoringscrabble.ui.common.menu.DropdownMenuExt
import com.zeroboss.scoringscrabble.ui.common.menu.DropdownMenuItemExt
import com.zeroboss.scoringscrabble.ui.dilogs.AboutDialog
import com.zeroboss.scoringscrabble.ui.dilogs.SelectPlayersDialog
import com.zeroboss.scoringscrabble.ui.theme.*
import com.zeroboss.scoringscrabble.ui.viewmodels.ScoringViewModel
import org.koin.androidx.compose.get

@ExperimentalAnimationApi
@ExperimentalMaterialApi
@Composable
fun ScoreSheet(
    navController: NavController
) {
    val scaffoldState = rememberScaffoldState()

    Scaffold(
        scaffoldState = scaffoldState,
        modifier = Modifier.fillMaxSize(),
        topBar = { AppBar(navController) },
        content = { Body(get()) }
    )
}

@Composable
fun AppBar(
    navController: NavController,
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
            text = "Score Sheet",
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

@ExperimentalAnimationApi
@Composable
fun Body(
    scoringViewModel: ScoringViewModel
) {
    val (showSelectPlayers, setShowSelectPlayers) = remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Blue50)
            .padding(bottom = 5.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
    }

    SelectPlayersDialog(
        showDialog = showSelectPlayers,
        setShowDialog = setShowSelectPlayers,
        get()
    )
}

@Composable
fun DropDownMenu(
    navController: NavController,
) {
    var expanded by remember { mutableStateOf(false) }
    val (showAbout, setShowAbout) = remember { mutableStateOf(false) }

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

            AboutDialog(
                showAbout = showAbout,
                setShowAbout = setShowAbout
            )

//        DropdownMenuItemExt(
//            onClick = {
//                expanded = false
//                setShowScoringRules(true)
//            }
//        ) {
//            Text(stringResource(R.string.scoring_rules))
//        }
//
//                DropdownMenuItemExt(
//                    onClick = {
//                        expanded = false
//                        navController.navigate(Screen.Statistics.route)
//                    }
//                ) {
//                    Text(stringResource(R.string.statistics))
//                }
//
//                Divider()
//
//                DropdownMenuItemExt(
//                    onClick = {
//                        expanded = false
//                        setShowBackupDialog(true)
//                    }
//                ) {
//                    Text(stringResource(R.string.backup_database))
//                }
//
//                DropdownMenuItemExt(
//                    onClick = {
//                        expanded = false
//                        setShowRestoreDialog(true)
//                    }
//                ) {
//                    Text(stringResource(R.string.restore_database))
//                }
//
//                if (CommonDb.getBackupFiles().isNotEmpty()) {
//                    DropdownMenuItemExt(
//                        onClick = {
//                            expanded = false
//                            setShowClearBackupsDialog(true)
//                        }
//                    ) {
//                        Text(stringResource(R.string.clear_backups_mi))
//                    }
//                }
//
//                DropdownMenuItemExt(
//                    onClick = {
//                        expanded = false
//                        setShowClearDataDialog(true)
//                    }
//                ) {
//                    Text(stringResource(R.string.clear_database))
//                }
//
//                Divider()
//
//                DropdownMenuItemExt(
//                    onClick = {
//                        expanded = false
//                        setShowAbout(true)
//                    }
//                ) {
//                    Text(stringResource(R.string.about))
//                }
//            }
//
//            ScoringRulesDialog(
//                showDialog = showScoringRules,
//                setShowDialog = setShowScoringRules,
//                scoringRulesViewModel = scoringRulesViewModel
//            )
//
//            BackupDataDialog(
//                showBackupDialog = showBackupDialog,
//                setShowBackupDialog = setShowBackupDialog
//            )
//
//            RestoreDataDialog(
//                get(),
//                showRestoreDialog = showRestoreDialog,
//                setShowRestoreDialog = setShowRestoreDialog
//            )
//
//            ClearBackupsDialog(
//                showClearBackupsDialog = showClearBackupsDialog,
//                setShowClearBackups = setShowClearBackupsDialog,
//            )
//
//            ClearDataDialog(
//                showClearData = showClearDataDialog,
//                setShowClearData = setShowClearDataDialog
//            )
        }
    }
}