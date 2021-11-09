package com.zeroboss.scoring500.ui.dialogs

import android.content.Context
import android.content.Intent
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
import com.zeroboss.scoring500.ui.common.*
import com.zeroboss.scoringscrabble.R
import com.zeroboss.scoringscrabble.data.common.CommonDb
import com.zeroboss.scoringscrabble.ui.common.ButtonData
import com.zeroboss.scoringscrabble.ui.common.MultipleButtonBar
import com.zeroboss.scoringscrabble.ui.common.RestartApp
import com.zeroboss.scoringscrabble.ui.common.ShowDialogText
import com.zeroboss.scoringscrabble.ui.theme.textTitleStyle
import com.zeroboss.scoringscrabble.ui.theme.warningTitle
import kotlinx.coroutines.*

@Composable
fun ClearDataDialog(
    showClearData: Boolean = false,
    setShowClearData: (Boolean) -> Unit
) {
    if (showClearData) {
        val context = LocalContext.current

        Dialog(
            onDismissRequest = { setShowClearData(false) }
        ) {
            Card(
                modifier = Modifier.size(390.dp, 300.dp),
                shape = RoundedCornerShape(20.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row {
                        Image(
                            modifier = Modifier.fillMaxWidth(fraction = .35F),
                            painter = painterResource(R.drawable.player),
                            contentDescription = ""
                        )

                        ShowDialogText(
                            style = warningTitle,
                            topPadding = 0.dp,
                            text = R.string.warning
                        )
                    }

                    ShowDialogText(
                        style = textTitleStyle,
                        text = R.string.warning_removal_text
                    )

                    MultipleButtonBar(
                        buttonData = listOf(
                            ButtonData(
                                " No ",
                                onClicked = { setShowClearData(false) },
                            ),

                            ButtonData(
                                "Yes",
                                onClicked = {
                                    CommonDb.clearBoxStore()
                                    RestartApp.restart(context)
                                }
                            )
                        ),
                        modifier = Modifier.padding(top = 20.dp)
                    )
                }
            }
        }
    }
}
