package com.amadiyawa.feature_users.presentation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import coil.Coil.imageLoader
import com.amadiyawa.feature_base.presentation.navigation.AppNavGraphProvider
import com.amadiyawa.feature_base.presentation.navigation.DynamicDestinationRegistry
import com.amadiyawa.feature_users.presentation.navigation.UserDestination
import com.amadiyawa.feature_users.presentation.navigation.UserListNavigation
import com.amadiyawa.feature_users.presentation.navigation.userGraph
import com.amadiyawa.feature_users.presentation.screen.userdetail.UserDetailViewModelOld
import com.amadiyawa.feature_users.presentation.screen.userlist.UserListViewModelOld
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

/**
 * Defines the Koin module for the presentation layer of the User feature.
 *
 * This module provides dependencies such as ViewModels, image loader, and navigation destinations
 * required for the presentation layer. It also registers the `UserDestination` with the
 * `DynamicDestinationRegistry` and defines the navigation graph for the User feature.
 */
internal val presentationModule = module {
    viewModelOf(::UserListViewModelOld)

    viewModelOf(::UserDetailViewModelOld)

    single { imageLoader(get()) }

    single { UserDestination }
    DynamicDestinationRegistry.register(UserDestination)

    single<AppNavGraphProvider> {
        object : AppNavGraphProvider {
            override val startDestination = UserListNavigation.route
            override val isMainStartDestination = true

            override fun NavGraphBuilder.build(navController: NavHostController) {
                userGraph(navController)
            }
        }
    }
}