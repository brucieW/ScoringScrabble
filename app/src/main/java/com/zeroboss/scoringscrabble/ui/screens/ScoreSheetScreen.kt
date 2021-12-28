package com.zeroboss.scoringscrabble.ui.screens

import android.graphics.Paint
import androidx.compose.animation.core.*
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.*
import androidx.navigation.NavController
import com.zeroboss.scoringscrabble.R
import com.zeroboss.scoringscrabble.data.common.ActiveStatus
import com.zeroboss.scoringscrabble.data.entities.*
import com.zeroboss.scoringscrabble.domain.model.WordInfoItem
import com.zeroboss.scoringscrabble.presentation.WordInfoViewModel
import com.zeroboss.scoringscrabble.ui.common.TileSettings
import com.zeroboss.scoringscrabble.ui.common.TileType
import com.zeroboss.scoringscrabble.ui.common.TopPanel
import com.zeroboss.scoringscrabble.ui.dialogs.UnusedTilesDialog
import com.zeroboss.scoringscrabble.ui.theme.*
import com.zeroboss.scoringscrabble.ui.viewmodels.ScoringSheetViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.compose.get

@Composable
fun ScoreSheet(
    navController: NavController,
    wordInfoViewModel: WordInfoViewModel
) {
    val scaffoldState = rememberScaffoldState()

    LaunchedEffect(key1 = true) {
        wordInfoViewModel.eventFlow.collectLatest { event ->
            when(event) {
                is WordInfoViewModel.UIEvent.ShowSnackbar -> {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = event.message
                    )
                }
            }
        }
    }

    Scaffold(
        scaffoldState = scaffoldState,
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopPanel(
                R.string.score_sheet,
                onClickReturn = { navController.popBackStack() }
            )
        },
        content = { Body(get()) }
    )
}

@Composable
fun Body(
    scoringViewModel: ScoringSheetViewModel
) {
    val colState = rememberLazyListState()
    val rowState = rememberLazyListState()
    val firstPlayerSelected by scoringViewModel.firstPlayerSelected

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        state = colState
    ) {
        if (!firstPlayerSelected) {
            item {
                Text(
                    text = stringResource(id = if (ActiveStatus.activeMatch!!.isTeamType()) R.string.first_team else R.string.first_player),
                    style = errorText,
                    modifier = Modifier.padding(start = 60.dp, top = 20.dp)
                )
            }
        }

        item {
            LazyRow(
                modifier = Modifier.fillMaxHeight(),
                state = rowState
            ) {
                item {
                    TurnsColumn()
                }

                if (ActiveStatus.activeMatch!!.isTeamType()) {
                    itemsIndexed(items = scoringViewModel.teams, itemContent = { index, team ->
                        PlayerTeamCard(
                            scoringViewModel,
                            index,
                            team = team
                        )
                    })
                } else {
                    itemsIndexed(items = scoringViewModel.players, itemContent = { index, player ->
                        PlayerTeamCard(
                            scoringViewModel,
                            index,
                            player = player,
                        )
                    })
                }

                item {
                    ScrabbleBoardLayout(scoringViewModel)
                }
            }
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
fun PlayerTeamCard(
    scoringViewModel: ScoringSheetViewModel,
    index: Int,
    player: Player? = null,
    team: Team? = null
) {
    val unusedTiles by remember { scoringViewModel.unusedTiles[index] }
    val activePlayer by scoringViewModel.activePlayer
    val activeTeam by scoringViewModel.activeTeam

    val (showUnusedTilesDialog, setShowUnusedTilesDialog) = remember { mutableStateOf(false) }

    UnusedTilesDialog(
        scoringViewModel,
        index,
        showUnusedTilesDialog,
        setShowUnusedTilesDialog
    )

    var borderWidth = 1.dp
    var borderColour = Color.Black

    if (team == null && player == activePlayer || team != null && team == activeTeam) {
        borderWidth = 3.dp
        borderColour = Color.Blue
    }

    Card(
        modifier = Modifier
            .width(140.dp)
            .padding(top = 20.dp, end = 10.dp),
        shape = RoundedCornerShape(20.dp),
        backgroundColor = Color.White,
        border = BorderStroke(borderWidth, borderColour),
        elevation = 10.dp
    ) {
        Column {
            val name = if (team == null) player!!.name else team.getTeamName()
            Text(
                text = name,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(6.dp)
                    .clickable {
                        if (team == null) {
                            scoringViewModel.setActivePlayer(player!!)
                        } else {
                            scoringViewModel.setActiveTeam(team)
                        }
                    },
                textAlign = TextAlign.Center,
                style = textTitleStyle,
                softWrap = false,
                overflow = TextOverflow.Ellipsis,
            )

            BlackDivider()

            (0..21).forEach { item ->
//                val data = scoringViewModel.turnData

                Row(
                    modifier = Modifier
                        .background(if (item % 2 == 0) lightGreen else Color.White)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("")
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
                    text = "Unused",
                    fontSize = 15.sp,
                    modifier = Modifier.clickable {
                        setShowUnusedTilesDialog(true)
                    }
                )

                Text(
                    text = unusedTiles.toString(),

                    Modifier.clickable {
                        setShowUnusedTilesDialog(true)
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
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        BoardHeader(scoringViewModel)
        ScrabbleBoard(scoringViewModel)
    }

}

object ScreenData {
    var screenWidth: Int = 0
    var screenHeight: Int = 0
    var tileWidth: Int = 0
}

@Composable
fun getTileWidth(): Int {
    ScreenData.screenWidth =
        with(LocalDensity.current) { LocalConfiguration.current.screenWidthDp }
    ScreenData.screenHeight =
        with(LocalDensity.current) { LocalConfiguration.current.screenHeightDp }
    ScreenData.tileWidth = 24

    return ScreenData.tileWidth
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BoardHeader(
    scoringViewModel: ScoringSheetViewModel
) {
    val cancelEnabled by scoringViewModel.cancelEnabled
    val directionEast by scoringViewModel.directionEast
    val directionSouth by scoringViewModel.directionSouth

    val tileWidth = getTileWidth()

    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(end = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(id = R.string.cancel_tile),
                style = smallerText
            )

            Image(
                bitmap = ImageBitmap.imageResource(id = R.drawable.backspace),
                contentDescription = "Cancel",
                Modifier
                    .clickable { scoringViewModel.clickedBackSpace() }
                    .size(tileWidth.dp)
                    .alpha(cancelEnabled)
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
                    text = stringResource(id = R.string.accept),
                    image = Icons.Rounded.Check,
                    onClick = { },
                    tint = Color.Green
                )

                TextWithIcon(
                    text = stringResource(id = R.string.skip_turn),
                    image = Icons.Rounded.CancelPresentation,
                    onClick = { },
                    tint = Color.Red
                )

                TextWithIcon(
                    text = stringResource(id = R.string.cancel_word),
                    image = Icons.Rounded.Refresh,
                    onClick = { }
                )
            }
        }
    }
}

enum class PulseState {
    None,
    GrowStart,
    GrowEnd
}

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

@Composable
fun Dictionary(
    width: Dp,
    wordInfoViewModel: WordInfoViewModel
) {
    val activeWords = mutableListOf<String>()
    val selectedWords = mutableListOf<Boolean>()

    wordInfoViewModel.wordList.forEach {
        val word by it
        activeWords.add(word)
    }

    wordInfoViewModel.selectedWords.forEach {
        val selected by it
        selectedWords.add(selected)
    }

    val state by wordInfoViewModel.state

    Card(
        modifier = Modifier
            .width(width)
            .padding(top = 10.dp, end = 10.dp),
        elevation = 10.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                activeWords.forEachIndexed { index, word ->
                    Box(
                        modifier = Modifier
                            .padding(top = 10.dp, start = 10.dp)
                            .clickable {
                                selectedWords.forEachIndexed { index, _ ->
                                    selectedWords[index] = false
                                }
                                selectedWords[index] = true
                                wordInfoViewModel.onSearch(activeWords[index])
                            }
                            .border(
                                BorderStroke(
                                    if (selectedWords[index]) 3.dp else 1.dp,
                                    Color.Black
                                )
                            ),
                    ) {
                        Text(
                            text = word,
                            style = smallerText,
                            modifier = Modifier.padding(10.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            if (state.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.padding(top = 20.dp, start = 50.dp)
                )
            }

            state.wordInfoItems.forEachIndexed { i, wordInfo ->
                if (i > 0) {
                    Spacer(modifier = Modifier.height(8.dp))
                }

                WordInfoItem(wordInfo)
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

@Composable
fun TurnData(
    playerTurnData: PlayerTurnData
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(if (playerTurnData.turnId % 2 == 0) lightGreen else Color.White),
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


