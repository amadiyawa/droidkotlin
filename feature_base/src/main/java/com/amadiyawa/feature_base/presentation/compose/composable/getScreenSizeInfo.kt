package com.amadiyawa.feature_base.presentation.compose.composable

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Data class representing screen size information.
 *
 * @property widthDp The width of the screen in density-independent pixels (dp).
 * @property heightDp The height of the screen in density-independent pixels (dp).
 *
 * @author Amadou Iyawa
 */
data class ScreenSizeInfo(val widthDp: Dp, val heightDp: Dp)

/**
 * Retrieves the screen size information in density-independent pixels (dp).
 *
 * @return A ScreenSizeInfo object containing the width and height of the screen in dp.
 *
 * @author Amadou Iyawa
 */
@Composable
fun getScreenSizeInfo(): ScreenSizeInfo {
    val configuration = LocalConfiguration.current
    val screenWidthDp = configuration.screenWidthDp.dp
    val screenHeightDp = configuration.screenHeightDp.dp

    return ScreenSizeInfo(widthDp = screenWidthDp, heightDp = screenHeightDp)
}