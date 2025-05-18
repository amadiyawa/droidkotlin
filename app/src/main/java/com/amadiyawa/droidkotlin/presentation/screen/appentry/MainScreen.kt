package com.amadiyawa.droidkotlin.presentation.screen.appentry

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.amadiyawa.droidkotlin.presentation.navigation.MainScaffoldedApp
import com.amadiyawa.feature_auth.presentation.navigation.authGraph
import com.amadiyawa.feature_base.domain.manager.UserSessionManager
import com.amadiyawa.feature_base.presentation.compose.composable.SetupSystemBars
import com.amadiyawa.feature_base.presentation.navigation.AppRoutes
import com.amadiyawa.feature_base.presentation.navigation.AppState
import com.amadiyawa.feature_base.presentation.navigation.DestinationPlacement
import com.amadiyawa.feature_base.presentation.navigation.NavigationRegistry
import com.amadiyawa.feature_base.presentation.navigation.rememberAppState
import com.amadiyawa.feature_base.presentation.theme.AppTheme
import com.amadiyawa.feature_billing.presentation.navigation.InvoiceNavigationApi
import com.amadiyawa.feature_onboarding.presentation.navigation.onboardingGraph
import com.amadiyawa.feature_profile.presentation.navigation.ProfileNavigationApi
import org.koin.compose.koinInject
import timber.log.Timber

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
    val navigationRegistry: NavigationRegistry = koinInject()
    val userSessionManager: UserSessionManager = koinInject()

    // State to track whether we're in the main graph
    val inMainGraph = remember { mutableStateOf(false) }

    // Initialize UserSessionManager
    LaunchedEffect(Unit) {
        userSessionManager.initialize()

        // Update navigation destinations based on user role
        navigationRegistry.updateVisibleDestinations(userSessionManager.currentRole.value)
    }

    // Observe role changes and update visible destinations
    val currentRole by userSessionManager.currentRole.collectAsState()
    LaunchedEffect(currentRole) {
        Timber.d("User role changed to: $currentRole")
        navigationRegistry.updateVisibleDestinations(currentRole)
    }

    // Get main start destination
    val mainStartDestination = navigationRegistry.getMainStartDestination(currentRole)
    Timber.d("Main start destination: $mainStartDestination")

    // Observe bottom bar destinations
    val bottomBarDestinations by navigationRegistry.visibleDestinations.collectAsState()
    Timber.d("Visible destinations: ${bottomBarDestinations.size}")

    // Observe navigation destinations
    val navigationDestinations by navigationRegistry.visibleDestinations.collectAsState()
    Timber.d("Visible destinations: ${navigationDestinations.size}")

    // Enhanced logging to debug destination placements
    LaunchedEffect(navigationDestinations) {
        val bottomBarItems = navigationDestinations.filter {
            it.placement == DestinationPlacement.BottomBar
        }
        Timber.d("Bottom bar items (${bottomBarItems.size}): ${bottomBarItems.map { it.route }}")
    }

    // Track when we enter/exit the main graph
    DisposableEffect(navController) {
        val listener = NavController.OnDestinationChangedListener { _, destination, _ ->
            val currentRoute = destination.route ?: ""

            // These are the routes that are part of the main section
            val mainSectionRoutes = listOf(
                InvoiceNavigationApi.Routes.INVOICE_LIST,
                ProfileNavigationApi.Routes.PROFILE_MAIN,
                "fallback"
            )

            // Simple check: if the current route contains any of the main section routes,
            // we're in the main section
            val isInMain = mainSectionRoutes.any { route ->
                currentRoute.contains(route)
            }

            // Debug the exact route for troubleshooting
            Timber.d("Navigation changed: route='$currentRoute', isInMain=$isInMain")

            // Update both state trackers - THIS IS THE KEY FIX
            inMainGraph.value = isInMain
            appState.setInMainGraph(isInMain)

            // Additional debug to verify state is being updated
            Timber.d("Updated navigation state: local=${inMainGraph.value}, appState=${appState.isInMainGraph.value}")
        }

        navController.addOnDestinationChangedListener(listener)

        onDispose {
            navController.removeOnDestinationChangedListener(listener)
        }
    }

    AppTheme {
        SetupSystemBars()

        MainScaffoldedApp(
            appState = appState,
            navigationDestinations = navigationDestinations,
            // We don't need to pass showNavigation as we're updating appState.isInMainGraph directly
            // but keeping it for safety
            showNavigation = inMainGraph.value,
        ) {
            NavHost(
                navController = navController,
                startDestination = AppRoutes.ONBOARDING_GRAPH,
                modifier = Modifier.fillMaxSize()
            ) {
                // ✅ 1. Onboarding
                onboardingGraph(navController)

                // ✅ 2. Auth
                authGraph(navController)

                // ✅ 3. Main app graph
                navigation(
                    startDestination = mainStartDestination.takeIf { it.isNotEmpty() } ?: "fallback",
                    route = AppRoutes.MAIN_GRAPH
                ) {
                    // Register all feature navigation's
                    with(navigationRegistry) {
                        registerFeatureNavigations(navController, currentRole)
                    }

                    // Fallback destination
                    composable("fallback") {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("No accessible features found")
                        }
                    }
                }
            }
        }
    }
}