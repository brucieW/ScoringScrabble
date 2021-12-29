package com.zeroboss.scoringscrabble.ui.screens.scoresheet

import android.graphics.Paint
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.BadgeBox
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.zeroboss.scoringscrabble.R
import com.zeroboss.scoringscrabble.data.entities.Letters
import com.zeroboss.scoringscrabble.ui.common.TileSettings
import com.zeroboss.scoringscrabble.ui.common.TileType
import com.zeroboss.scoringscrabble.ui.theme.errorText
import com.zeroboss.scoringscrabble.ui.viewmodels.ScoringSheetViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.compose.get


@Composable
fun ScrabbleBoard(
    scoringViewModel: ScoringSheetViewModel
) {
    val freqTileWidth = getTileWidth()
    val tileWidth = (freqTileWidth * 2).toFloat()
    val radius = tileWidth - tileWidth / 4
    val boardWidth = (freqTileWidth * 13).dp
    val boardHeight = (freqTileWidth * 12 + freqTileWidth / 2).dp
    val star = ImageBitmap.imageResource(id = R.drawable.starting_star)

    val isFirstPos by scoringViewModel.isFirstPos
    var currentState by remember { mutableStateOf(PulseState.None) }
    val transition = updateTransition(targetState = currentState, label = "radius")
    val growRadius by transition.animateFloat(
        label = "radius"
    ) { state ->
        when (state) {
            PulseState.None -> 0f
            PulseState.GrowStart -> radius * 2.5f
            PulseState.GrowEnd -> radius
        }
    }

    val scope = rememberCoroutineScope()

    val error by scoringViewModel.errorText

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (error.isNotEmpty()) {
            Text(
                modifier = Modifier
                    .padding(bottom = 5.dp)
                    .fillMaxWidth(),
                text = error,
                style = errorText,
                textAlign = TextAlign.Center
            )
        }

        Row {
            Column(
                modifier = Modifier.padding(top = 10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                for (tile in 'A'..'Z' step 2) {
                    Row() {
                        ShowFrequencyTile(scoringViewModel, tile, freqTileWidth)
                        ShowFrequencyTile(scoringViewModel, tile + 1, freqTileWidth)
                    }
                }

                ShowFrequencyTile(scoringViewModel, '[', freqTileWidth)
            }

            Column() {
                Surface(
                    modifier = Modifier
                        .size(boardWidth, boardHeight)
                        .padding(end = 10.dp)
                        .border(BorderStroke(1.dp, Color.Black))
                ) {
                    Canvas(
                        modifier = Modifier
                            .fillMaxSize()
                            .pointerInput(Unit) {
                                detectTapGestures(
                                    onPress = {
                                        scoringViewModel.setFirstPos(it.x, it.y)
                                        currentState = PulseState.GrowStart
                                        scope.launch {
                                            delay(300)
                                            currentState = PulseState.GrowEnd
                                        }
                                    }
                                )
                            }
                    ) {
                        var x = tileWidth * 2 - tileWidth / 3
                        var y = tileWidth - tileWidth / 4

                        for (col in 0..14) {
                            drawColumnText(this, tileWidth, x, y, ('A' + col).toString())
                            x += tileWidth + 2
                        }

                        x = tileWidth / 2
                        y = tileWidth + tileWidth * 3 / 4

                        for (col in 1..15) {
                            drawColumnText(this, tileWidth, x, y, col.toString())
                            y += tileWidth + 2
                        }

                        y = tileWidth

                        for (row in 0..14) {
                            x = tileWidth + tileWidth / 3

                            for (col in 0..14) {
                                val position = ('A' + col).toString()

                                if (row == 1) {
                                    scoringViewModel.tileStartX[col] = x
                                }

                                if (col == 1) {
                                    scoringViewModel.tileStartY[row] = y
                                }

                                drawTile(
                                    this,
                                    position + (row + 1).toString(),
                                    x,
                                    y,
                                    tileWidth,
                                    star
                                )

                                x += tileWidth + 2
                            }

                            y += tileWidth + 2
                        }

                        scoringViewModel.adjustLastStartItems(tileWidth)

                        if (isFirstPos) {
                            drawCircle(
                                color = Color.Red,
                                radius = growRadius,
                                center = scoringViewModel.currentPos,
                                alpha = 0.2f
                            )
                        }
                    }
                }

                Dictionary(boardWidth, get())
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ShowFrequencyTile(
    scoringViewModel: ScoringSheetViewModel,
    tile: Char,
    tileWidth: Int
) {
    val gameStarted by scoringViewModel.gameStarted

    val badgeCounts = scoringViewModel.tileCounts

    val offset = tile - 'A'
    val alpha = if (badgeCounts[offset].value == 0) 0.6f else 1f

    BadgeBox(
        badgeContent = {
            Text(
                text = badgeCounts[offset].value.toString()
            )
        },
        modifier = Modifier
            .padding(end = 20.dp, bottom = 10.dp)
            .alpha(alpha)
            .clickable {
                if (gameStarted && scoringViewModel.currentPos.isValid()) {
                    scoringViewModel.addToTurnData(Letters.get(tile))
                    scoringViewModel.addToTileCount(offset, -1)
                }
            }

    ) {
        Image(
            painter = painterResource(Letters.get(tile).image),
            contentDescription = "",
            modifier = Modifier
                .size(tileWidth.dp)
                .alpha(alpha)
        )
    }
}

fun drawColumnText(
    compose: DrawScope,
    tileWidth: Float,
    x: Float,
    y: Float,
    text: String
) {
    val paint = Paint().apply {
        textAlign = Paint.Align.CENTER
        textSize = tileWidth / 3 * 2
        color = android.graphics.Color.BLACK
    }

    compose.drawContext.canvas.nativeCanvas.drawText(
        text,
        x,
        y,
        paint
    )
}

fun drawTile(
    compose: DrawScope,
    position: String,
    x: Float,
    y: Float,
    side: Float,
    star: ImageBitmap
) {
    val tileInfo = TileSettings.getTileInfo(position)

    compose.drawRect(
        color = tileInfo.info.color,
        topLeft = Offset(x, y),
        size = Size(side, side)
    )

    val paint = Paint().apply {
        textAlign = Paint.Align.CENTER
        textSize = side / 2
        color = android.graphics.Color.WHITE
    }

    compose.drawContext.canvas.nativeCanvas.drawText(
        tileInfo.info.text,
        x + (side / 2),
        y + (side / 2) + (side * 0.1f),
        paint
    )

    if (tileInfo == TileType.Starting) {
        compose.drawImage(
            image = star,
            dstOffset = IntOffset(x.toInt(), y.toInt()),
            dstSize = IntSize(side.toInt(), side.toInt())
        )
    }
}


