package com.zeroboss.scoringscrabble.ui.common

import android.content.Context
import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity

enum class ScreenType {
    SMALL,
    MEDIUM,
    LARGE
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

object ScreenData {
    var screenWidth: Int = 0
    var screenHeight: Int = 0
    var tileWidth: Int = 0
    var screenType: ScreenType = ScreenType.SMALL
}

@Composable
fun getTileWidth(): Int {
    ScreenData.screenWidth = LocalConfiguration.current.screenWidthDp
    ScreenData.screenHeight = LocalConfiguration.current.screenHeightDp
    ScreenData.tileWidth = ScreenData.screenWidth / 18

    if (ScreenData.screenWidth < 480) {
        ScreenData.screenType = ScreenType.SMALL
    } else if (ScreenData.screenWidth in 481..900) {
        ScreenData.screenType = ScreenType.MEDIUM
    } else {
        ScreenData.screenType = ScreenType.LARGE
    }


    return ScreenData.tileWidth
}

