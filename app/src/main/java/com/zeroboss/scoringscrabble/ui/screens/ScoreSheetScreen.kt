package com.zeroboss.scoringscrabble.ui.screens

import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
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

    val directionEast by scoringViewModel.directionEast
    val directionSouth by scoringViewModel.directionSouth
    val firstPos by scoringViewModel.firstPos
    val letters by scoringViewModel.letters

    LazyRow(
        modifier = Modifier.fillMaxHeight(),
        state = rowState
    ) {
        item {
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
                            .fillMaxWidth()
                            .padding(top = 2.dp, bottom = 2.dp),
                        text = "$index",
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        items(items = scoringViewModel._players, itemContent = { player ->
            ScoringCard(
                scoringViewModel,
                player
            )
        })

        item {
            Column {
                Row() {
                    Column() {
                        Text(
                            text = "Player 1's Turn",
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

                    Column(
                        verticalArrangement = Arrangement.Center
                    ) {
                        IconButton(
                            modifier = Modifier.size(32.dp),
                            onClick = {
                            }
                        ) {
                            Icon(
                                Icons.Rounded.Check,
                                contentDescription = "",
                                tint = darkGreen
                            )
                        }
                        IconButton(
                            modifier = Modifier.size(32.dp),
                            onClick = {
                            }
                        ) {
                            Icon(
                                Icons.Rounded.Refresh,
                                contentDescription = "",
                                tint = Color.Black
                            )
                        }
                    }

                    Column(
                        modifier = Modifier.padding(start = 5.dp, top = 10.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Skip Turn",
                            style = smallerText
                        )

                        IconButton(
                            modifier = Modifier.size(32.dp),
                            onClick = {
                            }
                        ) {
                            Icon(
                                Icons.Rounded.CancelPresentation,
                                contentDescription = "",
                                tint = Color.Red
                            )
                        }
                    }

                    Column() {
                        Row() {
                            TextField(
                                value = firstPos,
                                onValueChange = { scoringViewModel.setFirstPos(it) },
                                label = { Text("First Pos") },
                                modifier = Modifier
                                    .width(130.dp)
                                    .padding(start = 20.dp, end = 10.dp, top = 5.dp)
                            )

                            TextField(
                                value = letters,
                                onValueChange = { scoringViewModel.setLetters(it) },
                                label = { Text("Letters") },
                                modifier = Modifier
                                    .width(160.dp)
                                    .padding(top = 5.dp)
                            )

                        }
                    }
                }

                Row(
                    modifier = Modifier
                        .padding(start = 35.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    ('A'..'O').forEach { letter ->
                        Text(
                            modifier = Modifier.padding(start = 14.dp, end = 14.dp),
                            text = "$letter",
                            textAlign = TextAlign.Center
                        )
                    }
                }

                Row() {
                    Column(
                        modifier = Modifier.padding(start = 10.dp, end = 4.dp),
                        horizontalAlignment = Alignment.End
                    ) {
                        (1..15).forEach { index ->
                            Text(
                                text = "$index",
                                modifier = Modifier.padding(top = 9.dp, bottom = 9.dp),
                                textAlign = TextAlign.Right
                            )
                        }

                    }

                    Column {
                        ShowBoard(scoringViewModel)
                    }
                }
            }
        }
    }
}

@Composable
fun ScoringCard(
    scoringViewModel: ScoringSheetViewModel,
    player: Player
) {
    val context = LocalContext.current

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
                        .padding(2.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (item < data.size) {
                        Text("50")
                        IconButton(
                            modifier = Modifier
                                .size(16.dp),
                            onClick = {
                                Toast.makeText(context, "Clicked", Toast.LENGTH_LONG).show()
                            }
                        ) {
                            Icon(
                                Icons.Rounded.Add,
                                contentDescription = "",
                                tint = Color.Black
                            )
                        }

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
                    .padding(10.dp),
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

@Composable
fun DirectionButton(
    direction: Boolean,
    onClick: () -> Unit,
    image: ImageVector,
    description: String
) {
    IconButton(
        modifier = Modifier.size(if (direction) 40.dp else 32.dp),
        onClick = { onClick() }
    ) {
        Icon(
            image,
            contentDescription = description,
            tint = if (direction) darkGreen else Color.Black
        )
    }


}

@Composable
fun ShowBoard(
    scoringViewModel: ScoringSheetViewModel
) {
    val density = LocalDensity.current
    val image = ImageBitmap.imageResource(id = R.drawable.scrabble_board)
    val side = with(density) { 600.dp.toPx() }.toInt()
    val letterBitmaps = mutableListOf<ImageBitmap>()

    scoringViewModel.letters.value.forEach {
        letterBitmaps.add(ImageBitmap.imageResource(id = Letters.get(it).image))
    }

    Canvas(
        modifier = Modifier
            .size(620.dp)
            .padding(end = 20.dp, bottom = 20.dp)
            .border(BorderStroke(1.dp, Color.Black))
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        val x = it.x
                        val y = it.y
                        val tileWidth = side / 15
                        val col = 'A' + (x / tileWidth).toInt()
                        val row = (y / tileWidth).toInt() + 1
                        scoringViewModel.setFirstPos("$col$row")
                    }
                )
            }
    ) {
        drawImage(
            image = image,
            dstOffset = IntOffset.Zero,
            dstSize = IntSize(side, side)
        )

        // Show tiles.
        val firstPos = convertPosition(scoringViewModel.firstPos.value)

        if (firstPos.isValid()) {
            var start = 215
            letterBitmaps.forEach {
                drawImage(
                    image = it,
                    dstOffset = IntOffset(375, start),
                    dstSize = IntSize(50, 50)
                )

                start += 53
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


