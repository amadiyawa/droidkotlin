package com.amadiyawa.droidkotlin.presentation.screen.appentry

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import com.amadiyawa.droidkotlin.presentation.navigation.AppNavHost
import com.amadiyawa.feature_base.presentation.navigation.AppState
import com.amadiyawa.feature_base.presentation.navigation.rememberAppState
import com.amadiyawa.feature_base.presentation.theme.AppTheme

/**
 * Composable function that serves as the entry point for the app.
 *
 * This function sets up the app's theme and layout, including the navigation host.
 *
 * @param windowSizeClass The window size class used to determine the layout configuration.
 * @param appState The state of the app, including the navigation controller.
 */
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun AppEntry(
    windowSizeClass: WindowSizeClass,
    appState: AppState = rememberAppState(windowSizeClass = windowSizeClass)
) {
    AppTheme {
        Scaffold(
            modifier = Modifier.semantics { testTagsAsResourceId = true },
            containerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.onBackground
        ) {
            Row(
                Modifier
                    .fillMaxSize()
                    .padding(it)
            ) {
                AppNavHost(
                    navHostController = appState.navController,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}