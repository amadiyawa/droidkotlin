package com.amadiyawa.feature_base.presentation.navigation

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import com.amadiyawa.feature_base.presentation.compose.composable.railItemColors
import timber.log.Timber

/**
 * A custom implementation of a NavigationRail component.
 *
 * This composable function creates a vertical navigation rail with a list of destinations.
 * Each destination is represented by an icon and an optional label. The currently selected
 * destination is highlighted, and clicking on a destination triggers a navigation action.
 *
 * @param modifier A [Modifier] to be applied to the NavigationRail.
 * @param destinations A list of [NavDestinationContract] representing the navigation destinations.
 * @param onNavigate A lambda function invoked when a destination is clicked, passing the selected [NavDestinationContract].
 * @param currentDestination The currently active [NavDestination], used to determine the selected state.
 */
@Composable
fun CustomNavRail(
    modifier: Modifier = Modifier,
    destinations: List<NavigationDestination>,
    onNavigate: (String) -> Unit,
    currentDestination: NavDestination?
) {
    // Sort destinations by order
    val sortedDestinations = remember(destinations) {
        destinations.sortedBy { it.order }
    }

    // Debug log
    Timber.d("Rail navigation items: ${sortedDestinations.size}")

    NavigationRail(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.surfaceVariant,
        contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
    ) {
        sortedDestinations.forEach { destination ->
            val selected = currentDestination?.hierarchy?.any {
                it.route == destination.route
            } == true

            // Debug each item
            Timber.d("Rail item: ${destination.route}, selected=$selected, current=${currentDestination?.route}")

            val title = stringResource(id = destination.title)
            NavigationRailItem(
                selected = selected,
                onClick = {
                    Timber.d("Clicked on nav rail item: ${destination.route}")
                    if (!selected) {
                        onNavigate(destination.route)
                    }
                },
                icon = {
                    Icon(
                        imageVector = if (selected) destination.selectedIcon else destination.unselectedIcon,
                        contentDescription = title
                    )
                },
                label = {
                    Text(text = title)
                },
                colors = railItemColors()
            )
        }
    }
}