package com.zeroboss.scoringscrabble.ui.screens

import android.graphics.Typeface
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.delay

@Composable
fun Splash(
    navController: NavController
) {
    SplashScreenContent()
    LaunchedEffect(
        key1 = "JumpToHome",
        block = {
            delay(3000)
            navController.popBackStack()
            navController.navigate(Navigation.ScoreSheet.route)
        }
    )
}

@Preview(showBackground = true)
@Composable
fun SplashScreenContent() {
    val doDrawing = remember { mutableStateOf(false) }

    val textPaint = Paint().asFrameworkPaint().apply {
        isAntiAlias = true
        textSize = 20f
        color = android.graphics.Color.BLUE
        typeface = Typeface.create(Typeface.MONOSPACE, Typeface.BOLD)
    }

    val screenWidthPx = with(LocalDensity.current) {
        (LocalConfiguration.current.screenHeightDp * density)
    }

    val animTranslate by animateFloatAsState(
        targetValue = screenWidthPx,
        animationSpec = TweenSpec(10000, easing = LinearEasing)
    )

    Canvas(
        modifier = Modifier.fillMaxSize(),
        onDraw = {
            translate(top = animTranslate * 0.92f) {
                scale(scale = 1f) {
                    drawIntoCanvas {
                        it.nativeCanvas.drawText(
                            "My Jetpack Compose Text",
                            0f,
                            120.dp.toPx(),
                            textPaint
                        )
                    }
                }
            }
        }
    )
}