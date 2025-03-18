package com.amadiyawa.feature_base.presentation.navigation

import androidx.compose.ui.util.trace
import androidx.navigation.NavHostController

class AppState(
    val navController: NavHostController
) {
    fun navigate(destination: AppNavigationDestination, route: String? = null){
        trace("Navigation: $destination"){
            navController.navigate(route ?: destination.route)
        }
    }

    fun onBackClick() {
        navController.popBackStack()
    }
}