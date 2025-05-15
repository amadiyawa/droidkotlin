package com.amadiyawa.droidkotlin.presentation.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.unit.dp
import com.amadiyawa.feature_base.presentation.navigation.AppState
import com.amadiyawa.feature_base.presentation.navigation.CustomBottomBar
import com.amadiyawa.feature_base.presentation.navigation.CustomNavRail
import com.amadiyawa.feature_base.presentation.navigation.DestinationPlacement
import com.amadiyawa.feature_base.presentation.navigation.NavigationDestination
import timber.log.Timber

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun AppScaffoldNavigation(
    appState: AppState,
    navigationDestinations: List<NavigationDestination>,
    content: @Composable () -> Unit
) {
    val currentDestination = appState.currentDestination

    // Debug logs for scaffold setup
    val bottomBarDestinations = navigationDestinations.filter {
        it.placement == DestinationPlacement.BottomBar
    }

    // Get the value of shouldShowBottomBar before using it
    val shouldShowBottomBar = appState.shouldShowBottomBar

    // Enhanced debug logging
    val isInMainGraph = appState.isInMainGraph.collectAsState().value
    Timber.d("AppScaffoldNavigation setup:")
    Timber.d("- Bottom bar destinations: ${bottomBarDestinations.size}")
    Timber.d("- Current route: ${currentDestination?.route}")
    Timber.d("- shouldUseNavRail: ${appState.shouldUseNavRail}")
    Timber.d("- isInMainGraph: $isInMainGraph")
    Timber.d("- shouldShowBottomBar: $shouldShowBottomBar")

    Scaffold(
        modifier = Modifier
            .semantics { testTagsAsResourceId = true }
            .safeDrawingPadding(),
        containerColor = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.onBackground,
        bottomBar = {
            // Only show bottom bar if there are destinations to show
            if (bottomBarDestinations.isNotEmpty() && shouldShowBottomBar) {
                Timber.d("✅ Showing bottom bar with ${bottomBarDestinations.size} destinations")
                CustomBottomBar(
                    destinations = bottomBarDestinations,
                    currentDestination = currentDestination,
                    onNavigate = { route -> appState.navigate(route) }
                )
            } else {
                Timber.d("❌ Not showing bottom bar: hasDestinations=${bottomBarDestinations.isNotEmpty()}, shouldShow=$shouldShowBottomBar")
            }
        }
    ) { innerPadding ->
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            val navRailDestinations = navigationDestinations.filter {
                it.placement == DestinationPlacement.NavRail
            }

            val shouldShowNavRail = appState.shouldShowNavRail

            // Only show nav rail if there are destinations to show
            if (navRailDestinations.isNotEmpty() && shouldShowNavRail) {
                Timber.d("Showing nav rail with ${navRailDestinations.size} destinations")
                CustomNavRail(
                    destinations = navRailDestinations,
                    currentDestination = currentDestination,
                    onNavigate = { route -> appState.navigate(route) },
                    modifier = Modifier.width(72.dp)
                )
            }

            Box(modifier = Modifier.fillMaxSize()) {
                content()
            }
        }
    }
}