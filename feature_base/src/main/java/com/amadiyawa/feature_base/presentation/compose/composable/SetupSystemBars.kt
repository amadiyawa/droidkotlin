package com.amadiyawa.feature_base.presentation.compose.composable

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import com.google.accompanist.systemuicontroller.rememberSystemUiController

/**
 * Configures the system bars' appearance for the application.
 *
 * This function sets the color of the system bars (status bar and navigation bar)
 * to match the application's surface color, ensuring a cohesive visual experience.
 * It uses the [rememberSystemUiController] to control the system UI and applies
 * the color using a [SideEffect].
 */
@Composable
fun SetupSystemBars() {
    val systemUiController = rememberSystemUiController()
    val surface = MaterialTheme.colorScheme.surface

    SideEffect { systemUiController.setSystemBarsColor(color = surface) }
}