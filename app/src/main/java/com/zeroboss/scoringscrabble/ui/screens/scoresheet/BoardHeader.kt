package com.zeroboss.scoringscrabble.ui.screens.scoresheet

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.zeroboss.scoringscrabble.R
import com.zeroboss.scoringscrabble.ui.theme.smallerText
import com.zeroboss.scoringscrabble.ui.viewmodels.ScoringSheetViewModel

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



