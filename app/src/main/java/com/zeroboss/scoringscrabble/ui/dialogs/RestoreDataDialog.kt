package com.zeroboss.scoringscrabble.ui.dialogs

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.zeroboss.scoring500.ui.common.menu.DialogText
import com.zeroboss.scoring500.ui.common.menu.DialogTextId
import com.zeroboss.scoringscrabble.R
import com.zeroboss.scoringscrabble.data.common.BACKUP_PATH
import com.zeroboss.scoringscrabble.data.common.CommonDb
import com.zeroboss.scoringscrabble.data.common.DATA_PATH
import com.zeroboss.scoringscrabble.ui.common.MultipleButtonBar
import com.zeroboss.scoringscrabble.ui.common.RestartApp
import com.zeroboss.scoringscrabble.ui.common.getTwoButtons
import com.zeroboss.scoringscrabble.ui.theme.darkGreen
import com.zeroboss.scoringscrabble.ui.theme.smallerText
import java.io.File

@Composable
fun RestoreDataDialog(
    showRestoreDialog: Boolean,
    setShowRestoreDialog: (Boolean) -> Unit
) {
    if (showRestoreDialog) {
        val items = CommonDb.getBackupFiles()

        val listState = rememberLazyListState()
        var selectedIndex by remember { mutableStateOf(if (items.size == 1) 0 else -1) }

        val context = LocalContext.current

        Dialog(
            onDismissRequest = { setShowRestoreDialog(false) }
        ) {
            Card(
                modifier = Modifier.size(390.dp, 340.dp),
                shape = RoundedCornerShape(20.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row {
                        Image(
                            modifier = Modifier
                                .padding(top = 40.dp)
                                .fillMaxWidth(fraction = .35F),
                            painter = painterResource(R.drawable.player),
                            contentDescription = ""
                        )

                        Column(
                            modifier = Modifier.fillMaxWidth(fraction = 1f)
                        ) {
                            DialogTextId(R.string.select_backup, textAlign = TextAlign.Left)

                            LazyColumn(
                                state = listState,
                                modifier = Modifier
                                    .padding(start = 10.dp, top = 10.dp)
                                    .height(180.dp),

                            ) {
                                itemsIndexed(items) { _, item ->
                                    Text(
                                        text = item.date,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .selectable(
                                                selected = item.index == selectedIndex,
                                                onClick = {
                                                    if (selectedIndex != item.index) {
                                                        selectedIndex = item.index
                                                    } else {
                                                        selectedIndex = -1
                                                    }
                                                }
                                            ),
                                        color = if (selectedIndex == item.index) darkGreen else Color.Black,
                                        fontWeight = if (selectedIndex == item.index) FontWeight.Bold else FontWeight.Normal
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    MultipleButtonBar(
                        buttonData = getTwoButtons(
                            firstButtonText = "Restore",
                            secondButtonText = "Cancel",
                            firstButtonEnabled = selectedIndex != -1,
                            onFirstButtonClicked = {
                                CommonDb.closeDatabase()
                                val backupFile = items[selectedIndex].date
                                val backupName = "$BACKUP_PATH/$backupFile/data.mdb"
                                val targetName = "$DATA_PATH/data.mdb"
                                File(backupName).copyTo(File(targetName), true)

                                RestartApp.restart(context)
                            },
                            onSecondButtonClicked = { setShowRestoreDialog(false) },
                        )
                    )

                    DialogText(
                        text = "Note: This will restart the program",
                        style = smallerText
                    )
                }
            }
        }
    }
}





