package com.zeroboss.scoringscrabble.ui.screens.scoresheet

import android.graphics.Paint
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
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
    val gameTurnData = scoringViewModel.gameTurnData.collectAsState()

    val freqTileWidth = getTileWidth()
    val tileWidth = (freqTileWidth * 2).toFloat()
    val radius = tileWidth - tileWidth / 4
    val boardWidth = (freqTileWidth * 13).dp
    val boardHeight = (freqTileWidth * 12 + freqTileWidth / 2).dp

    val images = listOf(
        ImageBitmap.imageResource(R.drawable.letter_a),
        ImageBitmap.imageResource(R.drawable.letter_b),
        ImageBitmap.imageResource(R.drawable.letter_c),
        ImageBitmap.imageResource(R.drawable.letter_d),
        ImageBitmap.imageResource(R.drawable.letter_e),
        ImageBitmap.imageResource(R.drawable.letter_f),
        ImageBitmap.imageResource(R.drawable.letter_g),
        ImageBitmap.imageResource(R.drawable.letter_h),
        ImageBitmap.imageResource(R.drawable.letter_i),
        ImageBitmap.imageResource(R.drawable.letter_j),
        ImageBitmap.imageResource(R.drawable.letter_k),
        ImageBitmap.imageResource(R.drawable.letter_l),
        ImageBitmap.imageResource(R.drawable.letter_m),
        ImageBitmap.imageResource(R.drawable.letter_n),
        ImageBitmap.imageResource(R.drawable.letter_o),
        ImageBitmap.imageResource(R.drawable.letter_p),
        ImageBitmap.imageResource(R.drawable.letter_q),
        ImageBitmap.imageResource(R.drawable.letter_r),
        ImageBitmap.imageResource(R.drawable.letter_s),
        ImageBitmap.imageResource(R.drawable.letter_t),
        ImageBitmap.imageResource(R.drawable.letter_u),
        ImageBitmap.imageResource(R.drawable.letter_v),
        ImageBitmap.imageResource(R.drawable.letter_w),
        ImageBitmap.imageResource(R.drawable.letter_x),
        ImageBitmap.imageResource(R.drawable.letter_y),
        ImageBitmap.imageResource(R.drawable.letter_z),
    )

    val star = ImageBitmap.imageResource(id = R.drawable.starting_star)

    val isFirstPos by scoringViewModel.isFirstPos
    val currentPos by scoringViewModel.currentPos

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
                                center = currentPos,
                                alpha = 0.2f
                            )
                        }

                        gameTurnData.value.forEach { turn ->
                            turn.letters.forEach { letter ->
                                drawImage(
                                    image = images[letter.letter.value],
                                    topLeft = Offset(100f, 100f)
                                )
                            }
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
                if (scoringViewModel.isGameStarted() &&
                    scoringViewModel.isCurrentPosSet()
                ) {
                    scoringViewModel.addToPlayerTurnData(Letters.get(tile))
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

