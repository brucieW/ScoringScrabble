package com.zeroboss.scoringscrabble.presentation.screens.scoresheet

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
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.zeroboss.scoringscrabble.R
import com.zeroboss.scoringscrabble.ui.theme.smallerText

@Composable
fun BoardHeader(
    scoringViewModel: ScoringSheetViewModel
) {
    val cancelEnabled by scoringViewModel.cancelEnabled
    val directionEast by scoringViewModel.directionEast
    val directionSouth by scoringViewModel.directionSouth

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
                    .padding(top = 5.dp)
                    .size(24.dp)
                    .alpha(cancelEnabled)
            )
        }

        Column() {
            Text(
                text = "Direction ",
                style = smallerText
            )

            Row() {
                IconButton(
                    modifier = Modifier.size(32.dp),
                    onClick = { scoringViewModel.setDirectionEast() }
                ) {
                    Icon(
                        imageVector = Icons.Rounded.ArrowForward,
                        contentDescription = "East",
                        tint = if (directionEast) Color.Green else Color.Gray
                    )
                }

                IconButton(
                    modifier = Modifier.size(32.dp),
                    onClick = { scoringViewModel.setDirectionSouth() }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_arrow_downward),
                        contentDescription = "South",
                        tint = if (directionSouth) Color.Green else Color.Gray
                    )
                }
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
                    image = Icons.Rounded.Clear,
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



