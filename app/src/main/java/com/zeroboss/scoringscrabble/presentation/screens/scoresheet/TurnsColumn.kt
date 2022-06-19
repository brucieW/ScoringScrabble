package com.zeroboss.scoringscrabble.presentation.screens.scoresheet

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun TurnsColumn() {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .padding(start = 10.dp, end = 10.dp, top = 22.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Turn",
            modifier = Modifier.padding(top = 4.dp, bottom = 8.dp)
        )

        (1..22).forEach { index ->
            Text(
                modifier = Modifier
                    .fillMaxWidth(),
                text = "$index",
                textAlign = TextAlign.Center
            )
        }
    }
}
