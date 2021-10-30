package com.zeroboss.scoringscrabble.ui.screens

import android.graphics.Typeface
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.zeroboss.scoringscrabble.R
import com.zeroboss.scoringscrabble.ui.theme.Blue50
import kotlinx.coroutines.delay

@Composable
fun Splash(
    navController: NavController
) {
    SplashScreenContent()
    LaunchedEffect(
        key1 = "JumpToHome",
        block = {
            delay(3500)
            navController.popBackStack()
            navController.navigate(Navigation.SelectPlayers.route)
        }
    )
}

@Preview(showBackground = true)
@Composable
fun SplashScreenContent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Blue50)
            .padding(bottom = 5.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Letter(R.drawable.letter_s)
            Letter(R.drawable.letter_c, top = 10.dp)
            Letter(R.drawable.letter_r)
            Letter(R.drawable.letter_a, top = 10.dp)
            Letter(R.drawable.letter_b)
            Letter(R.drawable.letter_b, top = 10.dp)
            Letter(R.drawable.letter_l)
            Letter(R.drawable.letter_e, top = 10.dp)
        }

        Letter(R.drawable.letter_s, start = 4.dp, top = 50.dp)
        Letter(R.drawable.letter_c, start = 4.dp, top = 2.dp)
        Letter(R.drawable.letter_o, start = 4.dp, top = 2.dp)
        Letter(R.drawable.letter_r, start = 4.dp, top = 2.dp)

        Row(
            modifier = Modifier
                .padding(top = 2.dp)
        ) {
            Letter(R.drawable.letter_s, start = 0.dp)
            Letter(R.drawable.letter_h)
            Letter(R.drawable.letter_e)
            Letter(R.drawable.letter_e)
            Letter(R.drawable.letter_t)
        }

    }
}

@Composable
fun Letter(
    image: Int,
    start: Dp = 2.dp,
    top: Dp = 0.dp
) {
    Card(
        modifier = Modifier
            .padding(start = start, top = top),
        elevation = 20.dp
    ) {
        Image(
            painterResource(image),
            "",
            contentScale = ContentScale.Fit
        )
    }
}
