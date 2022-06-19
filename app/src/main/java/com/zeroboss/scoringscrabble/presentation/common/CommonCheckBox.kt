package com.zeroboss.scoringscrabble.presentation.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.zeroboss.scoringscrabble.ui.theme.Blue800
import com.zeroboss.scoringscrabble.ui.theme.normalText

@Composable
fun CommonCheckBox(
    text: Int,
    checkStateChanged: (checked: Boolean) -> Unit,
    modifier: Modifier = Modifier,
    checked: Boolean = true,
    checkEndPadding: Dp = 10.dp,
    textStyle: TextStyle = normalText,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.SpaceBetween,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = horizontalArrangement,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(text),
            style = textStyle,
            modifier = Modifier.padding(end = 10.dp)
        )

        Checkbox(
            modifier = Modifier
                .padding(top = 10.dp, end = checkEndPadding),
            checked = checked,
            onCheckedChange = {
                checkStateChanged(it)
            },
            colors = CheckboxDefaults.colors(
                checkedColor = Blue800,
                uncheckedColor = Blue800,
                checkmarkColor = Color.White
            )
        )
    }
}