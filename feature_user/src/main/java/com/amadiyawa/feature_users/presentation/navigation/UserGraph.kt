package com.amadiyawa.feature_users.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.amadiyawa.feature_users.presentation.screen.userdetail.UserDetailScreen
import com.amadiyawa.feature_users.presentation.screen.userlist.UserListScreen

/**
 * Adds the user navigation graph to the [NavGraphBuilder].
 *
 * This function defines the navigation structure for the user-related screens,
 * including the user list and user detail screens. It specifies the start destination
 * and handles navigation between these screens.
 *
 * @param onNavigateToUserDetail A lambda function invoked with a user ID when navigating
 *                               to the user detail screen.
 * @param onBackClick A lambda function invoked when the back action is triggered.
 */
fun NavGraphBuilder.userGraph(
    onNavigateToUserDetail: (String) -> Unit,
    onBackClick: () -> Unit
) {
    navigation(
        startDestination = UserListNavigation.destination,
        route = UserListNavigation.route
    ) {
        composable(UserListNavigation.destination) {
            UserListScreen(onUserClick = onNavigateToUserDetail)
        }

        composable(UserDetailNavigation.route) { backStackEntry ->
            val uuid = backStackEntry.arguments?.getString("userId")
            if (uuid != null) {
                UserDetailScreen(uuid = uuid, onBackClick = onBackClick)
            }
        }
    }
}

/**
 * Adds the user navigation graph to the [NavGraphBuilder] using a [NavHostController].
 *
 * This function sets up the navigation structure for user-related screens by delegating
 * to the overloaded `userGraph` function. It defines the navigation actions for navigating
 * to the user detail screen and handling the back action.
 *
 * @param navController The [NavHostController] used to manage navigation actions.
 */
fun NavGraphBuilder.userGraph(navController: NavHostController) {
    userGraph(
        onNavigateToUserDetail = {
            navController.navigate(UserListNavigation.detailRoute(it))
        },
        onBackClick = {
            navController.popBackStack()
        }
    )
}