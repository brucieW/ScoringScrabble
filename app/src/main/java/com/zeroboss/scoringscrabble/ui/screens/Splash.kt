package com.zeroboss.scoringscrabble.ui.screens

import android.util.Log
import android.util.Log.INFO
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateIntOffsetAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.zeroboss.scoringscrabble.R
import com.zeroboss.scoringscrabble.data.entities.Letter
import com.zeroboss.scoringscrabble.data.entities.Letters
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
//    val splashViewModel by viewModel<SplashViewModel>()
//    splashViewModel.isAnimate.value = true

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
    val doAnimations = remember { animations.toMutableList()}

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Blue50)
            .padding(bottom = 5.dp)
    ) {
        Letter(
            'S',
            IntOffset(50, 200),
            doAnimations[0]
        )
        Letter(
            'C',
            IntOffset(85, 205),
            doAnimations[1]
        )
        Letter(
            'R',
            IntOffset(120, 200),
            doAnimations[2]
        )
        Letter(
            'A',
            IntOffset(155, 205),
            doAnimations[3]
        )
        Letter(
            'B',
            IntOffset(190, 200),
            doAnimations[4]
        )
        Letter(
            'B',
            IntOffset(225, 205),
            doAnimations[5]
        )
        Letter(
            'L',
            IntOffset(260, 200),
            doAnimations[6]
        )
        Letter(
            'E',
            IntOffset(295, 205),
            doAnimations[7]
        )

        Letter(
            'S',
            IntOffset(155, 260),
            doAnimations[8]
        )
        Letter(
            'C',
            IntOffset(155, 300),
            doAnimations[9]
        )
        Letter(
            'O',
            IntOffset(155, 340),
            doAnimations[10]
        )
        Letter(
            'R',
            IntOffset(155, 380),
            doAnimations[11]
        )
        Letter(
            'E',
            IntOffset(155, 420),
            doAnimations[12]
        )

        Letter(
            'S',
            IntOffset(85, 420),
            doAnimations[13]
        )
        Letter(
            'H',
            IntOffset(120, 420),
            doAnimations[14]
        )
        Letter(
            'E',
            IntOffset(190, 420),
            doAnimations[15]
        )
        Letter(
            'T',
            IntOffset(225, 420),
            doAnimations[16]
        )

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

    Card(
        modifier = Modifier
            .offset(target.x.dp, target.y.dp),
        elevation = 20.dp
    ) {
        Image(
            painterResource(id = Letters.get(tile).image),
            "",
            contentScale = ContentScale.Fit
        )
    }
}

