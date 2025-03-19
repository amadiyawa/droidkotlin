package com.amadiyawa.droidkotlin.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.amadiyawa.feature_onboarding.presentation.screen.navigation.OnboardListNavigation
import com.amadiyawa.feature_onboarding.presentation.screen.navigation.onboardListGraph

/**
 * Composable function that sets up the navigation host for the app.
 *
 * @param navHostController The controller for navigation host.
 * @param modifier The modifier to be applied to the NavHost.
 * @param startDestination The starting destination route for the NavHost.
 *
 * @author Amadou Iyawa
 */
@Composable
fun AppNavHost(
    navHostController: NavHostController,
    modifier: Modifier = Modifier,
    startDestination: String = OnboardListNavigation.route
) {
    NavHost(
        navController = navHostController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        onboardListGraph()
    }
}