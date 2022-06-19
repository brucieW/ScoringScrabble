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
import com.zeroboss.scoringscrabble.presentation.common.menu.DialogTextId
import com.zeroboss.scoringscrabble.R
import com.zeroboss.scoringscrabble.data.common.CommonDb
import com.zeroboss.scoringscrabble.presentation.common.MultipleButtonBar
import com.zeroboss.scoringscrabble.presentation.common.getYesNoButtons

@Composable
fun ClearBackupsDialog(
    showClearBackupsDialog: Boolean = false,
    setShowClearBackups: (Boolean) -> Unit
) {
    if (showClearBackupsDialog) {
        val context = LocalContext.current

        Dialog(
            onDismissRequest = { setShowClearBackups(false) }
        ) {
            Card(
                modifier = Modifier.size(390.dp, 240.dp),
                shape = RoundedCornerShape(20.dp)
            ) {
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
                                .fillMaxSize(fraction = .35F),
                            painter = painterResource(R.drawable.player),
                            contentDescription = ""
                        )

                        Column(
                            modifier = Modifier.fillMaxSize(fraction = 1f)
                        ) {
                            DialogTextId(R.string.clear_backups)
                            DialogTextId(R.string.are_you_sure)

                            MultipleButtonBar(
                                buttonData = getYesNoButtons(
                                    onYesClicked = {
                                        CommonDb.clearBackups(context)
                                        setShowClearBackups(false)
                                    },
                                    onNoClicked = { setShowClearBackups(false) }
                                ),
                                modifier = Modifier.padding(top = 20.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
