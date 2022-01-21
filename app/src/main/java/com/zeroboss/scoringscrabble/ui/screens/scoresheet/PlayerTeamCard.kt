package com.zeroboss.scoringscrabble.ui.screens.scoresheet

import androidx.compose.material.Surface
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zeroboss.scoringscrabble.data.entities.Player
import com.zeroboss.scoringscrabble.data.entities.Team
import com.zeroboss.scoringscrabble.ui.common.Direction
import com.zeroboss.scoringscrabble.ui.common.NumberPicker
import com.zeroboss.scoringscrabble.ui.theme.*
import com.zeroboss.scoringscrabble.ui.viewmodels.ScoringSheetViewModel

@Composable
fun PlayerTeamCard(
    scoringViewModel: ScoringSheetViewModel,
    index: Int,
    player: Player? = null,
    team: Team? = null
) {
    val unusedTiles = remember { scoringViewModel.unusedTiles[index] }
    val total = remember { scoringViewModel.total[index] }
    val totalMinusUnused = remember { scoringViewModel.totalMinusUnused[index] }

    val activePlayer by scoringViewModel.activePlayer
    val activeTeam by scoringViewModel.activeTeam

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
            val name = team?.getTeamName() ?: player!!.name

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

//            val gameData = ActiveStatus.gameTurnData

            (0..21).forEachIndexed { index, item ->
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
                    fontSize = 15.sp
                )

                NumberPicker(
                    state = unusedTiles,
                    modifier = Modifier,
                    Direction.Left,
                    0..30,
                    textStyle = smallerText,
                    onStateChanged = {
                        scoringViewModel.setUnused(index, it)
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
                    text = "${totalMinusUnused.value}",
                    fontSize = 20.sp,
                )
            }
        }
    }
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

