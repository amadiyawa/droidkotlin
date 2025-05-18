package com.amadiyawa.feature_profile.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Person
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.amadiyawa.feature_base.domain.util.UserRole
import com.amadiyawa.feature_base.presentation.navigation.AppRoutes
import com.amadiyawa.feature_base.presentation.navigation.DestinationPlacement
import com.amadiyawa.feature_base.presentation.navigation.FeatureNavigationApi
import com.amadiyawa.feature_base.presentation.navigation.NavigationDestination
import com.amadiyawa.feature_profile.R
import com.amadiyawa.feature_profile.presentation.screen.aboutscreen.AboutScreen
import com.amadiyawa.feature_profile.presentation.screen.profileedit.ProfileEditScreen
import com.amadiyawa.feature_profile.presentation.screen.profilemain.ProfileMainScreen
import com.amadiyawa.feature_profile.presentation.screen.profilesettings.ProfileSettingsScreen
import timber.log.Timber

/**
 * Navigation API implementation for the Profile feature.
 *
 * This module handles all profile-related functionality including:
 * - User profile view and edit
 * - Application settings
 * - About section
 * - Authentication management (sign out)
 *
 * It's designed as a template that can be extended for various projects.
 */
class ProfileNavigationApi : FeatureNavigationApi {

    override val featureId: String = "profile"

    // Profile should be accessible by all authenticated users
    override val allowedRoles: Set<UserRole> = setOf(
        UserRole.CLIENT,
        UserRole.ADMIN
    )

    // Profile is a main destination that appears in the bottom navigation
    override val isMainDestination: Boolean = true

    override fun NavGraphBuilder.registerNavigation(navController: NavHostController) {
        Timber.d("Registering Profile navigation graph")

        navigation(
            startDestination = Routes.PROFILE_MAIN,
            route = Routes.PROFILE_GRAPH
        ) {
            // Main profile screen with user info and menu items
            composable(route = Routes.PROFILE_MAIN) {
                Timber.d("Navigating to ProfileMainScreen")
                ProfileMainScreen(
                    onEditProfileClick = {
                        navController.navigate(Routes.PROFILE_EDIT)
                    },
                    onSettingsClick = {
                        navController.navigate(Routes.PROFILE_SETTINGS)
                    },
                    onAboutClick = {
                        navController.navigate(Routes.ABOUT)
                    },
                    onSignOutClick = {
                        // Handle sign out and navigate to authentication feature
                        Timber.d("Sign out requested")
                        navController.navigate(AppRoutes.AUTH_GRAPH) {
                            popUpTo(0) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                )
            }

            // Profile editing screen
            composable(route = Routes.PROFILE_EDIT) {
                Timber.d("Navigating to ProfileEditScreen")
                ProfileEditScreen(
                    onBackClick = {
                        navController.popBackStack()
                    },
                    onSaveClick = {
                        // Handle save action and navigate back
                        navController.popBackStack()
                    }
                )
            }

            // Settings screen
            composable(route = Routes.PROFILE_SETTINGS) {
                Timber.d("Navigating to ProfileSettingsScreen")
                ProfileSettingsScreen(
                    onBackClick = {
                        navController.popBackStack()
                    },
                    onThemeChanged = { /* Handle theme change */ },
                    onLanguageChanged = { /* Handle language change */ },
                    onNotificationSettingsClick = {
                        navController.navigate(Routes.NOTIFICATION_SETTINGS)
                    },
                    onPrivacyPolicyClick = {
                        navController.navigate(Routes.PRIVACY_POLICY)
                    },
                    onTermsOfServiceClick = {
                        navController.navigate(Routes.TERMS_OF_SERVICE)
                    }
                )
            }

            // About screen
            composable(route = Routes.ABOUT) {
                Timber.d("Navigating to AboutScreen")
                AboutScreen(
                    onBackClick = {
                        navController.popBackStack()
                    },
                    onContactUsClick = {
                        // Handle contact us action - email, website, etc.
                    },
                    onRateAppClick = {
                        // Handle rate app action - redirect to Play Store
                    }
                )
            }

            // Optional: Notification settings screen
            composable(route = Routes.NOTIFICATION_SETTINGS) {
                Timber.d("Navigating to NotificationSettingsScreen")
                // Implement NotificationSettingsScreen when needed
            }

            // Optional: Privacy policy screen
            composable(route = Routes.PRIVACY_POLICY) {
                Timber.d("Navigating to PrivacyPolicyScreen")
                // Implement PrivacyPolicyScreen when needed
            }

            // Optional: Terms of service screen
            composable(route = Routes.TERMS_OF_SERVICE) {
                Timber.d("Navigating to TermsOfServiceScreen")
                // Implement TermsOfServiceScreen when needed
            }

            // Advanced example: You could add deep linking support
            // This allows directly linking to specific profile sections
            composable(
                route = "${Routes.PROFILE_EDIT}/{${Routes.EDIT_SECTION_ARG}}",
                arguments = listOf(
                    navArgument(Routes.EDIT_SECTION_ARG) {
                        type = NavType.StringType
                        defaultValue = "general"
                    }
                ),
                deepLinks = listOf(
                    // Example deeplink: yourapp://profile/edit/password
                    // navDeepLink { uriPattern = "yourapp://profile/edit/{section}" }
                )
            ) { backStackEntry ->
                val section = backStackEntry.arguments?.getString(Routes.EDIT_SECTION_ARG)
                    ?: "general"

                Timber.d("Deep linking to ProfileEditScreen with section: $section")
                ProfileEditScreen(
                    section = section,
                    onBackClick = { navController.popBackStack() },
                    onSaveClick = { navController.popBackStack() }
                )
            }
        }

        Timber.d("Profile navigation graph registered successfully")
    }

    override fun getNavigationDestinations(): List<NavigationDestination> {
        return listOf(
            NavigationDestination(
                route = Routes.PROFILE_GRAPH,
                title = R.string.profile,
                selectedIcon = Icons.Filled.Person,
                unselectedIcon = Icons.Outlined.Person,
                placement = DestinationPlacement.BottomBar,
                order = 4 // Position in bottom bar - adjust based on your app's navigation
            )
        )
    }

    /**
     * Nested object containing all route constants for the Profile feature
     */
    object Routes {
        // Main navigation routes
        const val PROFILE_GRAPH = "profile_graph"
        const val PROFILE_MAIN = "profile_main"
        const val PROFILE_EDIT = "profile_edit"
        const val PROFILE_SETTINGS = "profile_settings"
        const val ABOUT = "about"
        const val NOTIFICATION_SETTINGS = "notification_settings"
        const val PRIVACY_POLICY = "privacy_policy"
        const val TERMS_OF_SERVICE = "terms_of_service"

        // Route arguments
        const val EDIT_SECTION_ARG = "section"

        // Helper methods for constructing routes with parameters
        fun editSectionRoute(section: String) = "$PROFILE_EDIT/$section"
    }
}