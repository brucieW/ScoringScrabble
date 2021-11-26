package com.zeroboss.scoringscrabble.ui.screens

import android.graphics.Paint
import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import androidx.navigation.NavController
import com.zeroboss.scoringscrabble.R
import com.zeroboss.scoringscrabble.data.entities.Letters
import com.zeroboss.scoringscrabble.data.entities.Player
import com.zeroboss.scoringscrabble.data.entities.TurnData
import com.zeroboss.scoringscrabble.ui.common.TileSettings
import com.zeroboss.scoringscrabble.ui.common.TileType
import com.zeroboss.scoringscrabble.ui.common.TopPanel
import com.zeroboss.scoringscrabble.ui.dialogs.UnusedTilesDialog
import com.zeroboss.scoringscrabble.ui.theme.*
import com.zeroboss.scoringscrabble.ui.viewmodels.ScoringSheetViewModel
import org.koin.androidx.compose.get

@ExperimentalAnimationApi
@ExperimentalMaterialApi
@Composable
fun ScoreSheet(
    navController: NavController
) {
    val scaffoldState = rememberScaffoldState()

    Scaffold(
        scaffoldState = scaffoldState,
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopPanel(
                R.string.score_sheet,
                onClickReturn = { navController.popBackStack() }
            )
        },
        content = { HomeBody(get()) }
    )
}

@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
@ExperimentalAnimationApi
@Composable
fun HomeBody(
    scoringViewModel: ScoringSheetViewModel
) {
    val rowState = rememberLazyListState()
    LazyRow(
        modifier = Modifier.fillMaxHeight(),
        state = rowState
    ) {
        item {
            TurnsColumn()
        }

        itemsIndexed(items = scoringViewModel._players, itemContent = { index, player ->
            ScoringCard(
                scoringViewModel,
                player,
                index
            )
        })

        item {
            ScrabbleBoardLayout(scoringViewModel)
        }
    }
}

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

@Composable
fun ScoringCard(
    scoringViewModel: ScoringSheetViewModel,
    player: Player,
    index: Int
) {
    val unusedTiles by remember { scoringViewModel.unusedTiles[index] }

    var showUnusedDialog by remember { mutableStateOf(false) }

    UnusedTilesDialog(
        scoringViewModel,
        index,
        showUnusedDialog,
        setShowDialog = { showUnusedDialog = false}
    )

    Card(
        modifier = Modifier
            .width(150.dp)
            .padding(top = 20.dp, end = 10.dp),
        shape = RoundedCornerShape(20.dp),
        backgroundColor = Color.White,
        border = BorderStroke(1.dp, Color.Black),
        elevation = 10.dp
    ) {
        Column {
            Text(
                text = player.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(6.dp),
                textAlign = TextAlign.Center,
                style = textTitleStyle
            )

            BlackDivider()

            (0..21).forEach { item ->
                val data = scoringViewModel.turnData

                Row(
                    modifier = Modifier
                        .background(if (item % 2 == 0) lightGreen else Color.White)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (item < data.size) {
                        Text("50")
                        Text("+")
                        Text("100")
                    } else {
                        Text("")
                    }
                }
            }

            BlackDivider()

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(lightSalmon)
                    .padding(start = 10.dp, end = 10.dp, top = 5.dp, bottom = 5.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Unused Tiles",
                    fontSize = 15.sp,
                    modifier = Modifier.clickable {
                        showUnusedDialog = true
                    }
                )

                Text(
                    text = unusedTiles.toString(),

                    Modifier.clickable {
                        showUnusedDialog = true
                    }
                )
            }

            BlackDivider(thickness = 3.dp)

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .background(Color.White),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = "Total",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "240",
                    fontSize = 20.sp,
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ScrabbleBoardLayout(
    scoringViewModel: ScoringSheetViewModel
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        BoardHeader(scoringViewModel)
        ScrabbleBoard(scoringViewModel)
    }

}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BoardHeader(
    scoringViewModel: ScoringSheetViewModel
) {
    val activePlayer by scoringViewModel.activePlayer
    val directionEast by scoringViewModel.directionEast
    val directionSouth by scoringViewModel.directionSouth
    val firstPos by scoringViewModel.firstPos

    val screenWidth = with(LocalDensity.current) { LocalConfiguration.current.screenWidthDp }
    val tileWidth = (screenWidth / 16)

    Column(
        modifier = Modifier.padding(10.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Row {
            Column(
                modifier = Modifier
                    .padding(end = 20.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = activePlayer.name,
                    style = textTitleStyle,
                    textAlign = TextAlign.Left,
                    modifier = Modifier.fillMaxWidth()
                )

                Image(
                    bitmap = ImageBitmap.imageResource(id = R.drawable.backspace),
                    contentDescription = "Backspace",
                    Modifier
                        .clickable { scoringViewModel.clickedBackSpace() }
                        .size(tileWidth.dp)
                        .padding(top = 5.dp)
                        .alpha(0.4f)
                )
            }

            Column(
                modifier = Modifier.padding(end = 10.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "First Pos",
                    style = smallerText
                )

                Text(
                    text = firstPos,
                    style = normalText,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Column() {
                Text(
                    text = "Direction ",
                    style = smallerText
                )

                Row() {
                    DirectionButton(
                        directionEast,
                        onClick = { scoringViewModel.setDirectionEast() },
                        image = Icons.Rounded.East,
                        description = "East Direction"
                    )

                    DirectionButton(
                        directionSouth,
                        onClick = { scoringViewModel.setDirectionSouth() },
                        image = Icons.Rounded.South,
                        description = "South Direction"
                    )
                }
            }

            Column(
                modifier = Modifier.padding(end = 10.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Row() {
                    TextWithIcon(
                        text = "Accept",
                        image = Icons.Rounded.Check,
                        onClick = { },
                        tint = Color.Green
                    )

                    TextWithIcon(
                        text = "Skip",
                        image = Icons.Rounded.CancelPresentation,
                        onClick = { },
                        tint = Color.Red
                    )

                    TextWithIcon(
                        text = "Refresh",
                        image = Icons.Rounded.Refresh,
                        onClick = { }
                    )

                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        ShowTiles(scoringViewModel, 'A', 'I', tileWidth)
        ShowTiles(scoringViewModel, 'J', 'R', tileWidth)
        ShowTiles(scoringViewModel, 'S', '[', tileWidth)

        Spacer(modifier = Modifier.height(10.dp))
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ShowTiles(
    scoringViewModel: ScoringSheetViewModel,
    start: Char,
    end: Char,
    tileWidth: Int
) {
    val badgeCounts = scoringViewModel.tileCounts

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        for (tile in start..end) {
            val offset = tile - 'A'
            val alpha = if (badgeCounts[offset].value == 0) 0.6f else 1f

            BadgeBox(
                badgeContent = {
                    Text(
                        text = badgeCounts[offset].value.toString()
                    )
                },
                modifier = Modifier
                    .padding(end = 20.dp)
                    .alpha(alpha)
                    .clickable {
                        scoringViewModel.addToTileCount(offset, -1)
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
    }
}

@Composable
fun TextWithIcon(
    text: String,
    image: ImageVector,
    onClick: () -> Unit,
    tint: Color = Color.Black,
) {
    Column(
        Modifier.padding(end = 10.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = text,
            style = smallerText
        )

        IconButton(
            modifier = Modifier.size(32.dp),
            onClick = { onClick() }
        ) {
            Icon(
                image,
                contentDescription = "",
                tint = tint
            )
        }

    }
}

@Composable
fun DirectionButton(
    direction: Boolean,
    onClick: () -> Unit,
    image: ImageVector,
    description: String
) {
    IconButton(
        modifier = Modifier.size(32.dp),
        onClick = { onClick() }
    ) {
        Icon(
            image,
            contentDescription = description,
            tint = if (direction) Color.Green else Color.Gray
        )
    }
}

@Composable
fun ScrabbleBoard(
    scoringViewModel: ScoringSheetViewModel
) {
    val density = LocalDensity.current
    val config = LocalConfiguration.current
    val screenWidth = with(density) { config.screenWidthDp.dp.toPx().toInt() }
//    val screenHeight = with(density) { config.screenHeightDp.dp.toPx().toInt() }
    val tileWidth = (screenWidth.toFloat() / 17.5f)
    val star = ImageBitmap.imageResource(id = R.drawable.starting_star)

    Surface(
        modifier = Modifier
            .size(
                config.screenWidthDp.dp - 5.dp,
                config.screenWidthDp.dp - 5.dp
            )
            .border(BorderStroke(1.dp, Color.Black))
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures(
                        onPress = { scoringViewModel.setFirstPos(it.x, it.y) }
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
//            val tileWidthInt = tileWidth.toInt()
//
//            for (turn in scoringViewModel.turnData) {
//                for (letter in turn.value.letters) {
//                    val xPos =
//                        scoringViewModel.tileStartX[letter.position.column].toInt() - tileWidthInt - 1
//                    val yPos =
//                        scoringViewModel.tileStartY[letter.position.row].toInt() - tileWidthInt - 1
//
//                    drawImage(
//                        image = scoringViewModel.tileImages[letter.letter.letter - 'A'],
//                        dstOffset = IntOffset(xPos, yPos),
//                        dstSize = IntSize(tileWidthInt, tileWidthInt)
//                    )
//                }
//            }
        }
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

@Composable
fun TurnData(
    turnData: TurnData
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(if (turnData.turn % 2 == 0) lightGreen else Color.White),
    ) {
        ScoreText("20")
        ScoreText("+")
        ScoreText("40")
    }

    Divider()
}

@Composable
fun BlackDivider(
    thickness: Dp = 1.dp
) {
    Divider(
        color = Color.Black,
        thickness = thickness
    )
}

@Composable
fun ScoreText(
    score: String
) {
    Text(
        text = score,
        textAlign = TextAlign.Center,
        style = normalText,
        modifier = Modifier.padding(5.dp)
    )
}


