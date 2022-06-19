package com.zeroboss.scoringscrabble.presentation.common

import android.content.Context
import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration

enum class ScreenType {
    SMALL,
    SMALL_SIDEWAYS,
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
    var isScreenSideways: Boolean = false

    fun isSmallScreen() : Boolean {
        return screenType == ScreenType.SMALL || screenType == ScreenType.SMALL_SIDEWAYS
    }
}

@Composable
fun getTileWidth(): Int {
    ScreenData.screenWidth = LocalConfiguration.current.screenWidthDp
    ScreenData.screenHeight = LocalConfiguration.current.screenHeightDp

    if (ScreenData.screenWidth < 480) {
        ScreenData.screenType = ScreenType.SMALL
    } else if (ScreenData.screenWidth in 481..900) {
        ScreenData.screenType = if (ScreenData.screenHeight < 480) ScreenType.SMALL_SIDEWAYS else ScreenType.MEDIUM
    } else {
        ScreenData.screenType = ScreenType.LARGE
    }

    ScreenData.isScreenSideways = ScreenData.screenWidth > ScreenData.screenHeight

    if (ScreenData.isScreenSideways) {
        ScreenData.tileWidth = ScreenData.screenHeight / 16
    } else {
        ScreenData.tileWidth = ScreenData.screenWidth / 16
    }

    return ScreenData.tileWidth
}

