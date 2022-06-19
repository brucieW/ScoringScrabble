package com.zeroboss.scoringscrabble.presentation.common

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.zeroboss.scoringscrabble.R
import com.zeroboss.scoringscrabble.ui.theme.smallerText

@Composable
fun ShowDialogText(
    style: TextStyle = smallerText,
    topPadding: Dp = 20.dp,
    text: Int = R.string.about_text
) {
    Text(
        modifier = Modifier
            .padding(top = topPadding)
            .fillMaxWidth(),
        style = style,
        text = stringResource(id = text),
        textAlign = TextAlign.Center
    )
}

@Preview
@Composable
fun ShowContent() {
    Card(
        modifier = Modifier.size(150.dp, 150.dp)
    ) {
        ShowDialogText()
    }
}
