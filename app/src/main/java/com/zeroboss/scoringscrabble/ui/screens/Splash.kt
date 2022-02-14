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
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.zeroboss.scoringscrabble.R
import com.zeroboss.scoringscrabble.ui.screens.destinations.HomeDestination
import com.zeroboss.scoringscrabble.ui.theme.Blue50
import kotlinx.coroutines.delay
import java.util.*
import kotlin.concurrent.schedule

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
    val doAnimation = remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Blue50)
            .padding(bottom = 5.dp)
    ) {
        Letter(
            painterResource(id = R.drawable.letter_s),
            IntOffset(50, 200),
            doAnimation
        )
        Letter(
            painterResource(id = R.drawable.letter_c),
            IntOffset(85, 205),
            doAnimation
        )
        Letter(
            painterResource(id = R.drawable.letter_r),
            IntOffset(120, 200),
            doAnimation
        )
        Letter(
            painterResource(id = R.drawable.letter_a),
            IntOffset(155, 205),
            doAnimation
        )
        Letter(
            painterResource(id = R.drawable.letter_b),
            IntOffset(190, 200),
            doAnimation
        )
        Letter(
            painterResource(id = R.drawable.letter_b),
            IntOffset(225, 205),
            doAnimation
        )
        Letter(
            painterResource(id = R.drawable.letter_l),
            IntOffset(260, 200),
            doAnimation
        )
        Letter(
            painterResource(id = R.drawable.letter_e),
            IntOffset(295, 205),
            doAnimation
        )

        Letter(
            painterResource(id = R.drawable.letter_s),
            IntOffset(155, 260),
            doAnimation
        )
        Letter(
            painterResource(id = R.drawable.letter_c),
            IntOffset(155, 300),
            doAnimation
        )
        Letter(
            painterResource(id = R.drawable.letter_o),
            IntOffset(155, 340),
            doAnimation
        )
        Letter(
            painterResource(id = R.drawable.letter_r),
            IntOffset(155, 380),
            doAnimation
        )
        Letter(
            painterResource(id = R.drawable.letter_e),
            IntOffset(155, 420),
            doAnimation
        )

        Letter(
            painterResource(id = R.drawable.letter_s),
            IntOffset(85, 420),
            doAnimation
        )
        Letter(
            painterResource(id = R.drawable.letter_h),
            IntOffset(120, 420),
            doAnimation
        )
        Letter(
            painterResource(id = R.drawable.letter_e),
            IntOffset(190, 420),
            doAnimation
        )
        Letter(
            painterResource(id = R.drawable.letter_t),
            IntOffset(225, 420),
            doAnimation
        )

        Timer("Wait", false).schedule(100) {
            doAnimation.value = true
        }
    }
}


@Composable
fun Letter(
    image: Painter,
    offset: IntOffset,
    doAnimation: MutableState<Boolean>
) {
    val target: IntOffset by animateIntOffsetAsState(
        targetValue = if (doAnimation.value) offset else IntOffset(0, 0),
        animationSpec = tween(
            durationMillis = 1000,
            easing = FastOutSlowInEasing
        )
    )

    Card(
        modifier = Modifier
            .offset(target.x.dp, target.y.dp),
        elevation = 20.dp
    ) {
        Image(
            image,
            "",
            contentScale = ContentScale.Fit
        )
    }
}

