package com.zeroboss.scoringscrabble.presentation.screens.statistics

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.zeroboss.scoringscrabble.R
import com.zeroboss.scoringscrabble.presentation.common.TopPanel
import com.zeroboss.scoringscrabble.ui.theme.Blue50
import com.zeroboss.scoringscrabble.ui.theme.smallerText
import com.zeroboss.scoringscrabble.ui.theme.textTitleStyle
import org.koin.androidx.compose.get

@Destination
@Composable
fun Statistics(
    navigator: DestinationsNavigator
) {
    val scaffoldState = rememberScaffoldState()

    Scaffold(
        scaffoldState = scaffoldState,
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopPanel(
                R.string.statistics,
                onClickReturn = {
                    navigator.popBackStack()
                }
            )
        },
        content = { StatsBody(get()) },
    )
}

@Composable
fun StatsBody(
    statisticsViewModel: StatisticsViewModel
) {
    val teamRankings = statisticsViewModel.teamRankings.collectAsState()
    val playerRankings = statisticsViewModel.playerRankings.collectAsState()

    Column(
        modifier = Modifier
            .background(Blue50)
            .fillMaxSize()
    ) {
        RankingCard(
            R.string.team_rankings,
            R.string.team_name,
            teamRankings.value)
        RankingCard(
            R.string.player_rankings,
            R.string.player_name,
            playerRankings.value,
            modifier = Modifier
                .padding(10.dp)
                .fillMaxSize()
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RankingCard(
    title: Int,
    columnText: Int,
    rankings: List<Ranking>,
    modifier: Modifier = Modifier
        .padding(10.dp)
        .fillMaxWidth()
        .fillMaxHeight(0.5f)
) {
    Card(
        modifier = modifier,
        elevation = 10.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Text(
                text = stringResource(id = title),
                textAlign = TextAlign.Center,
                style = textTitleStyle,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp)
            )

            val column1Weight = 0.14f
            val column2Weight = 0.45f
            val column3Weight = 0.28f

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp)
            ) {
                stickyHeader(
                    content = {
                        Row(
                            modifier = Modifier.background(Color.LightGray)
                        ) {

                            TableCell(
                                text = "Rank",
                                weight = column1Weight
                            )

                            TableCell(
                                text = stringResource(id = columnText),
                                weight = column2Weight
                            )
                            TableCell(
                                text = stringResource(id = R.string.wins_losses),
                                weight = column3Weight
                            )
                        }
                    }
                )

                itemsIndexed(rankings) { index, ranking ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (ranking.champion) {
                            Box(
                                modifier = Modifier
                                    .border(1.dp, Color.Black)
                                    .weight(column1Weight)
                                    .padding(8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Image(
                                    painterResource(id = R.drawable.trophy),
                                    contentDescription = ""
                                )
                            }
                        } else {
                            TableCell(
                                text = if (ranking.rank == 0) "" else ranking.rank.toString(),
                                weight = column1Weight,
                                center = true
                            )
                        }

                        TableCell(
                            text = ranking.name,
                            weight = column2Weight
                        )

                        val percent = ranking.percentWinsText()
                        val cellText = if (percent.isEmpty()) "" else "${ranking.wins}/${ranking.losses} (${percent}%) "

                        TableCell(
                            text = cellText,
                            weight = column3Weight
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun RowScope.TableCell(
    text: String,
    weight: Float,
    center: Boolean = false
) {
    Text(
        text = text,
        Modifier
            .border(1.dp, Color.Black)
            .weight(weight)
            .padding(8.dp),
        style = smallerText,
        textAlign = if (center) TextAlign.Center else TextAlign.Left
    )
}
