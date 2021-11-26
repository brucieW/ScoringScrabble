package com.zeroboss.scoringscrabble.ui.dialogs

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.zeroboss.scoringscrabble.ui.common.MultipleButtonBar
import com.zeroboss.scoringscrabble.ui.common.getTwoButtons
import com.zeroboss.scoringscrabble.ui.viewmodels.ScoringSheetViewModel

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun UnusedTilesDialog(
    scoringSheetViewModel: ScoringSheetViewModel,
    playerId: Int,
    showDialog: Boolean = false,
    setShowDialog: (Boolean) -> Unit
) {
    if (showDialog) {
        val text = scoringSheetViewModel.unusedTiles[playerId].value.toString()

        val unusedTiles = remember {
            mutableStateOf(
                TextFieldValue(
                    text = text,
                    selection = TextRange(0, text.length)
                )
            )
        }

        val focusRequester = remember { FocusRequester() }
        val keyboardController = LocalSoftwareKeyboardController.current

        Dialog(
            onDismissRequest = { setShowDialog(false) }
        ) {
            Card(
                modifier = Modifier.size(260.dp, 180.dp),
                shape = RoundedCornerShape(20.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    TextField(
                        value = unusedTiles.value.text,
                        onValueChange = { text -> unusedTiles.value = TextFieldValue(text) },
                        label = { Text("Unused Tiles Total Value ") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        maxLines = 1,
                        modifier = Modifier
                            .focusRequester(focusRequester)
                            .onFocusChanged { focusState ->
                                if (focusState.isFocused) {
                                    val focusText = unusedTiles.value.text
                                    unusedTiles.value = unusedTiles.value.copy(
                                        selection = TextRange(0, focusText.length)
                                    )
                                }
                            },
                    )

                    DisposableEffect(Unit) {
                        focusRequester.requestFocus()
                        keyboardController?.show()
                        onDispose { keyboardController?.hide() }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    MultipleButtonBar(
                        getTwoButtons(
                            firstButtonEnabled = true,
                            onFirstButtonClicked = {
                                scoringSheetViewModel.setUnusedTiles(
                                    offset = playerId,
                                    value = unusedTiles.value.text
                                )
                                setShowDialog(false)
                            },
                            onSecondButtonClicked = {
                                setShowDialog(false)
                            }
                        )
                    )
                }
            }
        }
    }
}

