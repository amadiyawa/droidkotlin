package com.amadiyawa.droidkotlin.presentation.screen.appentry

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.navigation
import com.amadiyawa.droidkotlin.presentation.navigation.MainScaffoldedApp
import com.amadiyawa.feature_auth.presentation.navigation.ForgotPasswordNavigation
import com.amadiyawa.feature_auth.presentation.navigation.OtpVerificationNavigation
import com.amadiyawa.feature_auth.presentation.navigation.SignInNavigation
import com.amadiyawa.feature_auth.presentation.navigation.SignUpNavigation
import com.amadiyawa.feature_auth.presentation.navigation.WelcomeNavigation
import com.amadiyawa.feature_auth.presentation.navigation.authGraph
import com.amadiyawa.feature_base.common.util.getMainStartDestination
import com.amadiyawa.feature_base.presentation.compose.composable.SetupSystemBars
import com.amadiyawa.feature_base.presentation.navigation.AppNavGraphProvider
import com.amadiyawa.feature_base.presentation.navigation.AppState
import com.amadiyawa.feature_base.presentation.navigation.rememberAppState
import com.amadiyawa.feature_base.presentation.theme.AppTheme
import com.amadiyawa.feature_onboarding.presentation.screen.navigation.OnboardListNavigation
import com.amadiyawa.feature_onboarding.presentation.screen.navigation.onboardListGraph
import org.koin.compose.getKoin

/**
 * MainScreen is a composable function that represents the main screen of the application.
 *
 * This function sets up the application's theme, initializes the navigation state,
 * and defines the navigation graphs for the onboarding process and the main application.
 *
 * @param windowSizeClass An instance of [WindowSizeClass] used to adapt the UI
 *                        based on the size of the window.
 * @param appState The state of the application, by default obtained via [rememberAppState].
 */
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun MainScreen(
    windowSizeClass: WindowSizeClass,
    appState: AppState = rememberAppState(windowSizeClass = windowSizeClass)
) {
    val navController = appState.navController
    val graphProviders = getKoin().getAll<AppNavGraphProvider>()
    val startDestination = rememberSaveable { mutableStateOf(OnboardListNavigation.route) }
    val mainAppGraphRoute = "main"
    val authGraphRoute = "auth"

    AppTheme {
        SetupSystemBars()
        MainScaffoldedApp(appState = appState) {
            NavHost(
                navController = navController,
                startDestination = startDestination.value,
                modifier = Modifier
                    .fillMaxSize()
            ) {
                // ✅ 1. Onboarding
                onboardListGraph(onFinished = {
                    navController.navigate(authGraphRoute) {
                        popUpTo(0) { inclusive = true }
                    }
                })

                // ✅ 2. Auth
                authGraph(navController)

                // ✅ 3. Main app graph
                navigation(
                    startDestination = getMainStartDestination(graphProviders),
                    route = mainAppGraphRoute
                ) {
                    graphProviders.forEach { provider ->
                        provider.run { build(navController) }
                    }
                }
            }
        }
    }
}