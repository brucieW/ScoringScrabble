package com.zeroboss.scoringscrabble.ui.dialogs

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.zeroboss.scoringscrabble.R
import com.zeroboss.scoringscrabble.ui.common.MultipleButtonBar
import com.zeroboss.scoringscrabble.ui.common.ShowDialogText
import com.zeroboss.scoringscrabble.ui.common.getYesNoButtons
import com.zeroboss.scoringscrabble.ui.theme.textTitleStyle

@Composable
fun DeleteGameDialog(
    showDialog: Boolean,
    setShowDialog: (Boolean) -> Unit,
    onYesClicked: () -> Unit
) {
    if (showDialog) {
        Dialog(
            onDismissRequest = { setShowDialog(false) }
        ) {
            Card(
                modifier = Modifier
                    .width(390.dp)
                    .height(300.dp),
                elevation = 10.dp
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        Modifier
                            .fillMaxSize(fraction = .35f)
                            .padding(start = 20.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            modifier = Modifier
                                .fillMaxSize(),

                            painter = painterResource(R.drawable.player),
                            contentDescription = ""
                        )
                    }

                    Column(
                        Modifier
                            .padding(start = 10.dp, end = 20.dp)
                            .fillMaxSize(fraction = 1f),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        ShowDialogText(
                            style = textTitleStyle,
                            text = R.string.warning_deleting_game
                        )

                        ShowDialogText(
                            style = textTitleStyle,
                            text = R.string.are_you_sure
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        MultipleButtonBar(
                            buttonData = getYesNoButtons(
                                onYesClicked = {
                                    onYesClicked()
                                    setShowDialog(false)
                                },

                                onNoClicked = {
                                    setShowDialog(false)
                                },
                            )
                        )
                    }
                }
            }
        }
    }
}
