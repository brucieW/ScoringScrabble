package com.zeroboss.scoringscrabble

import androidx.compose.animation.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.zeroboss.scoringscrabble.ui.screens.Navigation
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.zeroboss.scoringscrabble.ui.screens.ScoreSheet
import com.zeroboss.scoringscrabble.ui.screens.SelectPlayers
import com.zeroboss.scoringscrabble.ui.screens.Splash

@ExperimentalMaterialApi
@ExperimentalAnimationApi
@Composable
fun ScoringScrabbleApp() {
    val navController = rememberAnimatedNavController()

    AnimatedNavHost(
        navController = navController,
        startDestination = Navigation.Splash.route,

        ) {
        composable(
            Navigation.Splash.route,
        ) {
            Splash(navController)
        }

        composable(Navigation.SelectPlayers.route) {
            SelectPlayers(navController)
        }

        composable(Navigation.ScoreSheet.route) {
            ScoreSheet(navController)
        }
    }
}

