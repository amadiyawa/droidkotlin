package com.amadiyawa.feature_base.presentation.navigation

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import com.amadiyawa.feature_base.presentation.compose.composable.barItemColors

/**
 * Composable function that creates a custom bottom navigation bar.
 *
 * This function displays a navigation bar with items based on the provided destinations.
 * It highlights the currently selected destination and triggers navigation actions
 * when an item is clicked.
 *
 * @param modifier The [Modifier] to be applied to the navigation bar.
 * @param destinations A list of [NavDestinationContract] representing the navigation items.
 * @param onNavigate A lambda function invoked when a navigation item is clicked, passing the selected [NavDestinationContract].
 * @param currentDestination The current [NavDestination] being displayed, used to determine the selected item.
 */
@Composable
fun CustomBottomBar(
    modifier: Modifier = Modifier,
    destinations: List<NavDestinationContract>,
    onNavigate: (NavDestinationContract) -> Unit,
    currentDestination: NavDestination?
) {
    NavigationBar(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface
    ) {
        destinations.forEach { destination ->
            val selected = currentDestination?.hierarchy?.any {
                it.route == destination.route || it.route == destination.destination
            } == true
            val title = stringResource(id = destination.title)
            NavigationBarItem(
                selected = selected,
                onClick = {
                    if (!selected) {
                        onNavigate(destination)
                    }
                },
                icon = {
                    Icon(
                        imageVector = if (selected) destination.selectedIcon else destination.unselectedIcon,
                        contentDescription = title
                    )
                },
                label = { Text(text = title) },
                alwaysShowLabel = true,
                colors = barItemColors()
            )
        }
    }
}