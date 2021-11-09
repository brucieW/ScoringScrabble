package com.zeroboss.scoringscrabble

import androidx.compose.animation.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.zeroboss.scoringscrabble.ui.screens.*

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

        composable(
            Navigation.Home.route,
        ) {
            Home(navController)
        }

        composable(Navigation.SelectPlayers.route) {
            SelectPlayers(navController)
        }

        composable(Navigation.ScoreSheet.route) {
            ScoreSheet(navController)
        }
    }
}

