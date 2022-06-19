package com.zeroboss.scoringscrabble.presentation.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.zeroboss.scoringscrabble.ui.theme.Blue800
import com.zeroboss.scoringscrabble.ui.theme.navigationTitle2

@Composable
fun TopPanel(
    title: Int,
    onClickReturn: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Blue800)
            .height(40.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = { onClickReturn() }) {
            Icon(
                Icons.Rounded.ArrowBack,
                contentDescription = "",
                tint = Color.White,
                modifier = Modifier.padding(start = 10.dp),
            )
        }

        Text(
            text = stringResource(id = title),
            style = navigationTitle2,
            modifier = Modifier.padding(start = 20.dp)
        )
    }
}