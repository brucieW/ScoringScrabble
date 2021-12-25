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
import com.zeroboss.scoringscrabble.data.common.BlankTilePicker
import com.zeroboss.scoringscrabble.ui.common.MultipleButtonBar
import com.zeroboss.scoringscrabble.ui.common.getTwoButtons
import com.zeroboss.scoringscrabble.ui.viewmodels.ScoringSheetViewModel
import org.w3c.dom.Text

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun BlankTileDialog(
    scoringSheetViewModel: ScoringSheetViewModel,
    showBlankTilesDialog: Boolean,
    setShowBlankTilesDialog: (Boolean) -> Unit,
    onTileSelected: (Int) -> Unit
) {
    if (showBlankTilesDialog) {
        Dialog(
            onDismissRequest = { }
        ) {
            val letter = remember { mutableStateOf(0) }

            Card(
                modifier = Modifier.size(260.dp, 180.dp),
                shape = RoundedCornerShape(20.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    BlankTilePicker(
                        state = letter,
                        onStateChanged = { letter.value = it }
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    MultipleButtonBar(
                        getTwoButtons(
                            firstButtonEnabled = true,
                            onFirstButtonClicked = {
                                onTileSelected(letter.value)
                                setShowBlankTilesDialog(false)
                            },
                            onSecondButtonClicked = {
                                setShowBlankTilesDialog(false)
                            }
                        )
                    )
                }
            }
        }
    }
}
