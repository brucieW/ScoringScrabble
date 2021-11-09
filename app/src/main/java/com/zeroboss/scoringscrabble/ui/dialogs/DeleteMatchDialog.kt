package com.zeroboss.scoringscrabble.ui.dialogs

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.zeroboss.scoringscrabble.R
import com.zeroboss.scoringscrabble.ui.common.ButtonData
import com.zeroboss.scoringscrabble.ui.common.MultipleButtonBar
import com.zeroboss.scoringscrabble.ui.common.ShowDialogText
import com.zeroboss.scoringscrabble.ui.theme.textTitleStyle
import com.zeroboss.scoringscrabble.ui.theme.warningTitle

@Composable
fun DeleteMatchDialog(
    showDialog: Boolean,
    setShowDialog: (Boolean) -> Unit,
    onYesClicked: () -> Unit
) {
    if (showDialog) {
        Dialog(
            onDismissRequest = { setShowDialog(false) }
        ) {
            Card(
                modifier = Modifier.size(390.dp, 340.dp),
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
                            modifier = Modifier
                                .fillMaxWidth(fraction = .35F),
                            painter = painterResource(R.drawable.player),
                            contentDescription = "player"
                        )

                        ShowDialogText(
                            style = warningTitle,
                            topPadding = 0.dp,
                            text = R.string.delete_match
                        )
                    }

                    ShowDialogText(
                        style = textTitleStyle,
                        text = R.string.warning_deleting_match
                    )

                    ShowDialogText(
                        style = textTitleStyle,
                        text = R.string.are_you_sure
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    MultipleButtonBar(
                        buttonData = listOf(
                            ButtonData(
                                " No ",
                                onClicked = {
                                    setShowDialog(false)
                                },
                            ),

                            ButtonData(
                                "Yes",
                                onClicked = {
                                    onYesClicked()
                                    setShowDialog(false)
                                }
                            )
                        )
                    )
                }
            }
        }
    }
}
