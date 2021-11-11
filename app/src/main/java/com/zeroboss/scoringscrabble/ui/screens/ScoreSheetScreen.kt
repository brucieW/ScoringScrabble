package com.zeroboss.scoringscrabble.ui.screens

import android.icu.text.Transliterator
import android.widget.EditText
import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.zeroboss.scoringscrabble.R
import com.zeroboss.scoringscrabble.data.entities.Player
import com.zeroboss.scoringscrabble.data.entities.TurnData
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
    val context = LocalContext.current
    val density = LocalDensity.current

    LazyRow(
        modifier = Modifier.fillMaxHeight(),
        state = rowState
    ) {
        item {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(start = 10.dp, end = 10.dp, top = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Turn",
                    modifier = Modifier.padding(top = 4.dp, bottom = 4.dp)
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

        items(items = scoringViewModel._players, itemContent = { player ->
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
                            .padding(4.dp),
                        textAlign = TextAlign.Center,
                        style = textTitleStyle
                    )

                    Divider(color = Color.Black)

                    (0..21).forEach { item ->
                        val data = scoringViewModel.turnData

                        Row(
                            modifier = Modifier
                                .background(if (item % 2 == 0) lightGreen else Color.White)
                                .padding(start = 4.dp, end = 4.dp)
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

//                    Row(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .background(lightSalmon),
//                        horizontalArrangement = Arrangement.SpaceBetween
//                    ) {
//                        Text(
//                            text = "Overtime Penalty",
//                            modifier = Modifier.weight(0.5f)
//                        )
//
//                        Text(
//                            text = "25",
//                            modifier = Modifier.weight(0.5f)
//                        )
//
//                        Divider()
//                    }

                    Divider(color = Color.Black)

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(lightSalmon)
                            .padding(10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Unused Tiles",
                            //                            modifier = Modifier.weight(0.5f)
                        )

                        Text(
                            text = "3",
                            //                            modifier = Modifier.weight(0.5f)
                        )
                    }

                    Divider(color = Color.Black, thickness = 3.dp)

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
        })

        item {
            Column {
                Row() {
                    Column() {
                        Text(
                            text = "Player 1's Turn",
                            style = textTitleStyle,
                            modifier = Modifier.padding(start = 10.dp, top = 10.dp, end = 10.dp)
                        )

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(start = 10.dp)
                        ) {
                            Text(
                                text = "Direction: ",
                                style = textTitleStyle
                            )

                            IconButton(
                                modifier = Modifier.size(32.dp),
                                onClick = {
                                }
                            ) {
                                Icon(
                                    Icons.Rounded.East,
                                    contentDescription = "",
                                    tint = Color.Black
                                )
                            }

                            IconButton(
                                modifier = Modifier.size(32.dp),
                                onClick = {
                                }
                            ) {
                                Icon(
                                    Icons.Rounded.South,
                                    contentDescription = "",
                                    tint = Color.Black
                                )
                            }
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
                                tint = Color.Green
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
                                value = "A1",
                                onValueChange = {},
                                label = { Text("First Position") },
                                modifier = Modifier
                                    .width(160.dp)
                                    .padding(start = 20.dp, end = 10.dp, top = 5.dp)
                            )

                            TextField(
                                value = "Abet",
                                onValueChange = {},
                                label = { Text("Letters") },
                                modifier = Modifier
                                    .width(160.dp)
                                    .padding(top = 5.dp)
                            )
                        }
                    }
                }

                Image(
                    painter = painterResource(id = R.drawable.scrabble_board),
                    contentDescription = "Scrabble Board",
                    modifier = Modifier
                        .padding(start = 20.dp, top = 10.dp, end = 20.dp)
                        .size(600.dp)
                        .pointerInput(Unit) {
                            detectTapGestures(
                                onPress = {
                                    val x = it.x
                                    val y = it.y
                                    val px = with(density) { 600.dp.toPx() }
                                    val tileWidth = px / 15
                                    val col = 'A' + (x / tileWidth).toInt()
                                    val row = (y / tileWidth).toInt() + 1
                                    Toast
                                        .makeText(
                                            context,
                                            "X = $x, Y = $y, tile = $col$row, 600dp = $px",
                                            Toast.LENGTH_LONG
                                        )
                                        .show()
                                }
                            )
                        }
                )
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


