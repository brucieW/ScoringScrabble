package com.zeroboss.scoringscrabble.data.common

import android.service.quicksettings.Tile
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp

@Composable
fun BoardTile() {
    val showScoreSheet = remember { mutableStateOf(true) }
    val scale = remember { mutableStateOf(1f) }

    if (showScoreSheet.value) {

    }

    Box(
        modifier = Modifier
            .clip(RectangleShape)
            .padding(5.dp)
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTransformGestures { _, _, zoom, _ ->
                    scale.value *= zoom
                }
            }
    ) {

    }
}