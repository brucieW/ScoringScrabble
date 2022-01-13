package com.zeroboss.scoringscrabble

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.SideEffect
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.ramcosta.composedestinations.DestinationsNavHost
import com.zeroboss.scoringscrabble.ui.screens.NavGraphs
import com.zeroboss.scoringscrabble.ui.theme.Blue50
import com.zeroboss.scoringscrabble.ui.theme.ScoringScrabbleTheme

@ExperimentalAnimationApi
@ExperimentalMaterialApi
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val systemUiController = rememberSystemUiController()
            val useDarkIcons = MaterialTheme.colors.isLight

            SideEffect {
                systemUiController.setSystemBarsColor(Blue50, darkIcons = useDarkIcons)
            }

            ScoringScrabbleTheme {
                Surface() {
                    DestinationsNavHost(navGraph = NavGraphs.root)
                }
            }
        }
    }
}

