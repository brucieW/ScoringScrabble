package com.zeroboss.scoringscrabble.presentation.common.menu

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.zeroboss.scoringscrabble.ui.theme.textTitleStyle

@Composable
fun DialogTextId(
    text: Int,
    style: TextStyle = textTitleStyle,
    textAlign: TextAlign = TextAlign.Center
) {
    DialogText(stringResource(id = text), style, textAlign)
}

@Composable
fun DialogText(
    text: String,
    style: TextStyle = textTitleStyle,
    textAlign: TextAlign = TextAlign.Center
) {
    Text(
        modifier = Modifier
            .padding(top = 20.dp)
            .fillMaxWidth(),
        style = style,
        text = text,
        textAlign = textAlign
    )
}
