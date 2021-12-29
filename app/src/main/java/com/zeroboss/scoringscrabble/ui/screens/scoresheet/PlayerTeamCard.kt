package com.zeroboss.scoringscrabble.ui.screens.scoresheet

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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zeroboss.scoringscrabble.data.entities.Player
import com.zeroboss.scoringscrabble.data.entities.PlayerTurnData
import com.zeroboss.scoringscrabble.data.entities.Team
import com.zeroboss.scoringscrabble.ui.dialogs.UnusedTilesDialog
import com.zeroboss.scoringscrabble.ui.theme.lightGreen
import com.zeroboss.scoringscrabble.ui.theme.lightSalmon
import com.zeroboss.scoringscrabble.ui.theme.normalText
import com.zeroboss.scoringscrabble.ui.theme.textTitleStyle
import com.zeroboss.scoringscrabble.ui.viewmodels.ScoringSheetViewModel


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
