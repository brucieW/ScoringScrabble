package com.zeroboss.scoringscrabble.presentation.dialogs

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.zeroboss.scoringscrabble.presentation.common.menu.DialogText
import com.zeroboss.scoringscrabble.presentation.common.menu.DialogTextId
import com.zeroboss.scoringscrabble.R
import com.zeroboss.scoringscrabble.data.common.CommonDb
import com.zeroboss.scoringscrabble.data.common.DATE_PATTERN
import com.zeroboss.scoringscrabble.presentation.common.MultipleButtonBar
import com.zeroboss.scoringscrabble.presentation.common.getYesNoButtons
import com.zeroboss.scoringscrabble.ui.theme.smallerText
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun BackupDataDialog(
    showBackupDialog: Boolean,
    setShowBackupDialog: (Boolean) -> Unit
) {
    if (showBackupDialog) {
        val name = LocalDateTime.now()
            .format(DateTimeFormatter.ofPattern(DATE_PATTERN))

        Dialog(
            onDismissRequest = { setShowBackupDialog(false) }
        ) {
            Card(
                modifier = Modifier
                    .width(390.dp)
                    .height(250.dp),
                shape = RoundedCornerShape(20.dp)
            ) {
                val context = LocalContext.current

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 20.dp, end = 10.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row {
                        Image(
                            modifier = Modifier
                                .padding(top = 40.dp)
                                .fillMaxSize(fraction = .35f),
                            painter = painterResource(R.drawable.player),
                            contentDescription = ""
                        )

                        Column(
                            modifier = Modifier.fillMaxSize(fraction = 1f)
                        ) {
                            DialogTextId(R.string.backup_tag)
                            DialogText(text = name, style = smallerText)
                            DialogTextId(R.string.are_you_sure)

                            MultipleButtonBar(
                                modifier = Modifier.padding(top = 20.dp),
                                buttonData = getYesNoButtons(
                                    onYesClicked = {
                                        CommonDb.backupData(context, name)
                                        setShowBackupDialog(false)
                                    },
                                    onNoClicked = { setShowBackupDialog(false) }
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}


