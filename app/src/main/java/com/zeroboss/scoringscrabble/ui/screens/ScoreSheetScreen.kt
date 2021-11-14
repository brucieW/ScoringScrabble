package com.zeroboss.scoringscrabble.ui.screens

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.zeroboss.scoringscrabble.R
import com.zeroboss.scoringscrabble.data.entities.Letters
import com.zeroboss.scoringscrabble.data.entities.Player
import com.zeroboss.scoringscrabble.data.entities.TurnData
import com.zeroboss.scoringscrabble.data.entities.convertPosition
import com.zeroboss.scoringscrabble.ui.common.TopPanel
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
        content = { ScoreSheetBody(get()) }
    )
}

@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
@ExperimentalAnimationApi
@Composable
fun ScoreSheetBody(
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

        items(items = scoringViewModel._players, itemContent = { player ->
            ScoringCard(
                scoringViewModel,
                player
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
    player: Player
) {
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
                    .padding(5.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Unused Tiles",
                )

                Text(
                    text = "3",
                )
            }

            BlackDivider(thickness = 3.dp)

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .background(Color.White),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Total"
                )

                Text(text = "240")
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

@Composable
fun BoardHeader(
    scoringViewModel: ScoringSheetViewModel
) {
    val activePlayer by scoringViewModel.activePlayer
    val directionEast by scoringViewModel.directionEast
    val directionSouth by scoringViewModel.directionSouth
    val firstPos by scoringViewModel.firstPos
    val letters by scoringViewModel.letters

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row {
            Column() {
                Text(
                    text = "${activePlayer.name}'s Turn",
                    style = textTitleStyle,
                    modifier = Modifier.padding(start = 20.dp, top = 10.dp, end = 10.dp)
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(start = 20.dp)
                ) {
                    Text(
                        text = "Direction: ",
                        style = textTitleStyle
                    )

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

            Column() {
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
                        onClick = { /*TODO*/ },
                        tint = Color.Red
                    )

                    TextWithIcon(
                        text = "Refresh",
                        image = Icons.Rounded.Refresh,
                        onClick = { /*TODO*/ }
                    )

                    Column(
                        modifier = Modifier.padding(top = 10.dp),
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
                }
                TilesSelector(
                    start = 'A',
                    end = 'Z'
                )

            }

        }
    }
}

@Composable
fun TilesSelector(
    start: Char,
    end: Char
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        for (tile in start..end) {
            Text(
                text = tile.toString(),
                modifier = Modifier
            )
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
        Modifier.padding(top = 10.dp, end = 10.dp),
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
        modifier = Modifier.size(40.dp),
        onClick = { onClick() }
    ) {
        Icon(
            image,
            contentDescription = description,
            tint = if (direction) darkGreen else Color.Gray
        )
    }


}

@Composable
fun ScrabbleBoard(
    scoringViewModel: ScoringSheetViewModel
) {
    val density = LocalDensity.current
    val image = ImageBitmap.imageResource(id = R.drawable.scrabble_board)
    val letterBitmaps = mutableListOf<ImageBitmap>()

    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp.dp

    val sideInPixels = with(density) { screenWidth.toPx().toInt() }

    val tileWidth = sideInPixels / 16

    scoringViewModel.letters.value.forEach {
        letterBitmaps.add(ImageBitmap.imageResource(id = Letters.get(it).image))
    }

    Canvas(
        modifier = Modifier
            .size(screenWidth)
            .border(BorderStroke(1.dp, Color.Black))
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        val x = it.x - tileWidth

                        if (x >= 0) {
                            val y = it.y - tileWidth
                            val col = 'A' + (x / tileWidth).toInt()
                            val row = (y / tileWidth).toInt() + 1
                            scoringViewModel.setFirstPos("$col$row")
                        }
                    }
                )
            }
    ) {
        drawImage(
            image = image,
            dstOffset = IntOffset.Zero,
            dstSize = IntSize(sideInPixels, sideInPixels)
        )

        // Show tiles.
        val firstPos = convertPosition(scoringViewModel.firstPos.value)

        if (firstPos.isValid()) {
            var x = ('A' + (tileWidth * firstPos.column)).code

            if (screenWidth < screenHeight) {
                x -= tileWidth
            } else {
                x += tileWidth
            }

            var y = (tileWidth * firstPos.row)

            letterBitmaps.forEach {
                drawImage(
                    image = it,
                    dstOffset = IntOffset(x, y),
                    dstSize = IntSize(tileWidth, tileWidth)
                )

                if (scoringViewModel.directionEast.value) {
                    x += tileWidth
                } else {
                    y += tileWidth
                }
            }
        }

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


