package com.amadiyawa.droidkotlin.presentation.navigation

import androidx.compose.runtime.Composable
import com.amadiyawa.feature_base.presentation.navigation.AppState

/**
 * Composable function that represents the main scaffolded application.
 *
 * This function sets up the application's main structure, including navigation and content.
 * It uses the provided [AppState] to manage the current navigation state and renders
 * the content within a scaffolded layout.
 *
 * @param appState The [AppState] instance managing the application's navigation state.
 * @param content A composable lambda that defines the content to be displayed within the scaffold.
 */
@Composable
fun MainScaffoldedApp(
    appState: AppState,
    content: @Composable () -> Unit
) {
    val currentDestination = appState.currentDestination

    AppScaffoldNavigation(
        appState = appState,
        currentDestination = currentDestination,
        content = content
    )
}