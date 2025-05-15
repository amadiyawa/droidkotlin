package com.amadiyawa.feature_base.presentation.navigation

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import timber.log.Timber

/**
 * Represents the state of the application, including navigation, window size, and coroutine scope.
 *
 * This class provides properties and methods to manage the application's navigation state,
 * determine UI layout based on window size, and handle navigation actions.
 *
 * @property navController The [NavHostController] used to manage navigation actions.
 * @property windowSizeClass The [WindowSizeClass] representing the current window size.
 * @property coroutineScope The [CoroutineScope] used for launching coroutines within the app state.
 */
@Stable
class AppState(
    val navController: NavHostController,
    val windowSizeClass: WindowSizeClass,
    val coroutineScope: CoroutineScope
) {
    val currentDestination: NavDestination?
        @Composable get() = navController.currentBackStackEntryAsState().value?.destination

    val shouldUseNavRail: Boolean
        get() = windowSizeClass.widthSizeClass >= WindowWidthSizeClass.Medium

    private val _isInMainGraph = MutableStateFlow(false)
    val isInMainGraph: StateFlow<Boolean> = _isInMainGraph.asStateFlow()

    val shouldShowBottomBar: Boolean
        @Composable get() = !shouldUseNavRail && isInMainGraph.collectAsState().value

    val shouldShowNavRail: Boolean
        @Composable get() = shouldUseNavRail && isInMainGraph.collectAsState().value

    /**
     * Sets whether the user is currently in the main navigation graph
     * where navigation elements should be displayed.
     *
     * @param inMain True if in the main graph, false otherwise
     */
    fun setInMainGraph(inMain: Boolean) {
        _isInMainGraph.value = inMain
    }

    // In AppState.kt
    fun navigate(route: String) {
        try {
            Timber.d("Navigating to $route")
            navController.navigate(route) {
                popUpTo(navController.graph.findStartDestination().id) {
                    saveState = true
                }
                launchSingleTop = true
                restoreState = true
            }
        } catch (e: Exception) {
            Timber.e(e, "Navigation error: $route")
        }
    }

    fun onBackClick() {
        navController.popBackStack()
    }
}

/**
 * Remembers and provides an instance of [AppState].
 *
 * This function creates and remembers an [AppState] instance, which manages the application's
 * navigation state, window size, and coroutine scope. It uses default values for the
 * [NavHostController] and [CoroutineScope] if not provided.
 *
 * @param navController The [NavHostController] used to manage navigation actions. Defaults to a remembered instance.
 * @param windowSizeClass The [WindowSizeClass] representing the current window size.
 * @param coroutineScope The [CoroutineScope] used for launching coroutines within the app state. Defaults to a remembered instance.
 * @return An instance of [AppState] configured with the provided or default parameters.
 */
@Composable
fun rememberAppState(
    navController: NavHostController = rememberNavController(),
    windowSizeClass: WindowSizeClass,
    coroutineScope: CoroutineScope = rememberCoroutineScope()
): AppState {
    return remember(navController, windowSizeClass, coroutineScope) {
        AppState(navController, windowSizeClass, coroutineScope)
    }
}