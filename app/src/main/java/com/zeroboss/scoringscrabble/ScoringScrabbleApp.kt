package com.zeroboss.scoringscrabble

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.zeroboss.scoringscrabble.ui.screens.*

@Composable
fun ScoringScrabbleApp() {
    val navController = rememberNavController()

    NavHost(
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

