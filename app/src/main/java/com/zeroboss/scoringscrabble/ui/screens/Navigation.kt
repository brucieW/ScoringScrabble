package com.zeroboss.scoringscrabble.ui.screens

sealed class Navigation(val route: String) {
    object Splash : Navigation("splash")
    object SelectPlayers : Navigation("select_players")
    object ScoreSheet : Navigation("score_sheet")
}
