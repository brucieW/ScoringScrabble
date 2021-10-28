package com.zeroboss.scoring500.ui.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import com.zeroboss.scoringscrabble.ui.common.TrailingIcon
import com.zeroboss.scoringscrabble.ui.theme.typography

@Composable
fun TextWithDropDown(
    modifier: Modifier = Modifier,
    text: String,
    onClickedDropDown: () -> Unit,
    onFocusAltered: () -> Unit,
) {
    val focusRequester = FocusRequester()

    Row(
        modifier = modifier
            .fillMaxWidth()
            .focusRequester(focusRequester)
            .onFocusChanged {
                if (it.hasFocus) {
                    onFocusAltered()
                }
            },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,

    ) {
        Text(
            text = text,
            style = typography.h6
        )

        TrailingIcon(
            onTrailingIconClicked = onClickedDropDown,
            focusRequester = focusRequester
        )
    }
}