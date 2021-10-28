package com.zeroboss.scoringscrabble.ui.common

import android.content.Context
import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration

@Composable
fun isSmallScreen(): Boolean {
    return LocalConfiguration.current.screenWidthDp < 600
}

object RestartApp {
    fun restart(context: Context) {
        val manager = context.getPackageManager()
        val intent = manager.getLaunchIntentForPackage(context.packageName)
        val componentName = intent?.component
        val mainIntent = Intent.makeRestartActivityTask(componentName)
        context.startActivity(mainIntent)
        Runtime.getRuntime().exit(0)
    }
}