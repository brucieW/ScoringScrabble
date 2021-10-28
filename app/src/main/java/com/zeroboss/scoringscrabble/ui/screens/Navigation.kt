package com.zeroboss.scoringscrabble.ui.screens

sealed class Navigation(val route: String) {
    object Splash : Navigation("splash")
    object ScoreSheet : Navigation("score_sheet")
}
