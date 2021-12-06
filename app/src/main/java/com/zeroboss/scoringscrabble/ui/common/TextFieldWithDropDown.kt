package com.zeroboss.scoringscrabble.ui.common

import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.zeroboss.scoringscrabble.ui.theme.Blue300
import com.zeroboss.scoringscrabble.ui.theme.Blue600
import com.zeroboss.scoringscrabble.ui.theme.Blue800
import com.zeroboss.scoringscrabble.ui.theme.normalText

@Composable
fun TextFieldWithDropDown(
    modifier: Modifier = Modifier,
    text: String,
    onNameChanged: (String) -> Unit = {},
    label: String = "Test Label",
    backgroundColor: Color = Color.White,
    onTrailingIconClicked: () -> Unit,
    onFocusAltered: () -> Unit = {}
) {
    val focusRequester = FocusRequester()

    OutlinedTextField(
        modifier = modifier
            .focusRequester(focusRequester)
            .onFocusChanged {
                if (it.hasFocus) {
                    onFocusAltered()
                }
            },
        textStyle = normalText,
        value = text,
        singleLine = true,
        onValueChange = { onNameChanged(it) },
        trailingIcon = { TrailingIcon(onTrailingIconClicked, focusRequester) },
        label = { Text(text = label) },
        colors = TextFieldDefaults.textFieldColors(
            cursorColor = Blue600,
            backgroundColor = backgroundColor,
            focusedLabelColor = Blue800,
            unfocusedLabelColor = Blue300,
            focusedIndicatorColor = Blue800,
            unfocusedIndicatorColor = Blue300,
            textColor = Blue800
        )
    )
}
