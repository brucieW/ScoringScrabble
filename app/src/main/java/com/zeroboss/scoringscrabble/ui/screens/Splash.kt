package com.zeroboss.scoringscrabble.ui.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateIntOffsetAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.zeroboss.scoringscrabble.data.entities.Letter
import com.zeroboss.scoringscrabble.data.entities.Letters
import com.zeroboss.scoringscrabble.ui.common.ScreenData
import com.zeroboss.scoringscrabble.ui.common.ScreenType
import com.zeroboss.scoringscrabble.ui.common.getTileWidth
import com.zeroboss.scoringscrabble.ui.screens.destinations.HomeDestination
import com.zeroboss.scoringscrabble.ui.theme.Blue50
import kotlinx.coroutines.delay
import java.util.*
import kotlin.concurrent.scheduleAtFixedRate

@Destination(start = true)
@Composable
fun Splash(
    navigator: DestinationsNavigator
) {
    SplashScreenContent()

    LaunchedEffect(
        key1 = "JumpToHome",
        block = {
            delay(4000)
            navigator.navigate(HomeDestination())
        }
    )
}

@Composable
fun SplashScreenContent() {
    val animations = Array<MutableState<Boolean>>(17) { mutableStateOf(false) }
    val doAnimations = remember { animations.toMutableList() }

    val tileWidth = getTileWidth()
    val spacer = tileWidth * 2

//    if (ScreenData.screenType == ScreenType.SMALL) {
//        tileWidth += (tileWidth / 2)
//        spacer += (tileWidth / 2)
//    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Blue50)
    ) {
        var x = (ScreenData.screenWidth - (tileWidth * 15)) / 2
        val top = (ScreenData.screenHeight - (spacer * 8)) / 2
        var y = top

        "SCRABBLE".forEachIndexed { index, tile ->
            Letter(
                tile,
                IntOffset(x, y),
                doAnimations[index]
            )

            x += spacer
            y = if (y == top) top + 5 else top
        }

        x -= spacer * 5
        y += (spacer * 2) - (spacer / 2)

        "SCORE".forEachIndexed { index, tile ->
            Letter(
                tile,
                IntOffset(x, y) ,
                doAnimations[index + 8])
            y += spacer
        }

        x -= spacer * 2

        "SHEET".forEachIndexed { index, tile ->
            Letter(
                tile,
                IntOffset(x, y),
                doAnimations[index + 12]
            )

            x += spacer
        }

        var offset = 0

        Timer("Wait").scheduleAtFixedRate(
            delay = 100,
            period = 100,
        ) {
            doAnimations[offset++].value = true

            if (offset == 17) {
                cancel()
            }
        }
    }
}


@Composable
fun Letter(
    tile: Char,
    offset: IntOffset,
    doAnimation: MutableState<Boolean>
) {
    val target: IntOffset by animateIntOffsetAsState(
        targetValue = if (doAnimation.value) offset else IntOffset(0, 0),
        animationSpec = tween(
            durationMillis = 500,
            easing = FastOutSlowInEasing
        )
    )

    val size = (ScreenData.tileWidth * 2).dp

    Card(
        modifier = Modifier
            .offset(target.x.dp, target.y.dp)
            .size(size),
        elevation = 20.dp
    ) {
        Image(
            painterResource(id = Letters.get(tile).image),
            "",
            contentScale = ContentScale.Fit
        )
    }
}

