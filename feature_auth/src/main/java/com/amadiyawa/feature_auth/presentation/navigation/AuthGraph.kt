package com.amadiyawa.feature_auth.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.amadiyawa.feature_auth.presentation.screen.forgotpassword.ForgotPasswordScreen
import com.amadiyawa.feature_auth.presentation.screen.otpverification.OtpVerificationScreen
import com.amadiyawa.feature_auth.presentation.screen.resetpassword.ResetPasswordScreen
import com.amadiyawa.feature_auth.presentation.screen.signin.SignInScreen
import com.amadiyawa.feature_auth.presentation.screen.signup.SignUpScreen
import com.amadiyawa.feature_auth.presentation.screen.welcome.WelcomeScreen

fun NavGraphBuilder.authGraph(
    onSignIn: () -> Unit,
    onNavigateToSignUp: () -> Unit,
    onNavigateToForgotPassword: () -> Unit,
    onSignUpSuccess: (recipient: String) -> Unit,
    onOtpValidated: () -> Unit,
    onOtpFailed: () -> Unit,
    onResetPasswordSuccess: () -> Unit
) {
    navigation(
        startDestination = WelcomeNavigation.destination,
        route = "auth"
    ) {
        composable(WelcomeNavigation.destination) {
            WelcomeScreen(
                onSignIn = onSignIn,
                onSignUp = onNavigateToSignUp
            )
        }

        composable(SignInNavigation.route) {
            SignInScreen(
                onSignInSuccess = onOtpValidated,
                onForgotPassword = onNavigateToForgotPassword
            )
        }

        composable(SignUpNavigation.route) {
            SignUpScreen(
                onSignUpSuccess = onSignUpSuccess
            )
        }

        composable(
            route = OtpVerificationNavigation.route,
            arguments = listOf(navArgument("recipient") { type = NavType.StringType })
        ) { backStackEntry ->
            val recipient = backStackEntry.arguments?.getString("recipient")
            OtpVerificationScreen(
                recipient = recipient ?: "",
                onOtpValidated = onOtpValidated,
                onCancel = onOtpFailed
            )
        }

        composable(ForgotPasswordNavigation.route) {
            ForgotPasswordScreen(
                onOtpSent = { recipient ->
                    // handled in graph navController
                }
            )
        }

        composable(ResetPasswordNavigation.route) {
            ResetPasswordScreen(
                onSuccess = onResetPasswordSuccess
            )
        }
    }
}

fun NavGraphBuilder.authGraph(navController: NavHostController) {
    authGraph(
        onSignIn = { navController.navigate(SignInNavigation.route) },
        onNavigateToSignUp = { navController.navigate(SignUpNavigation.route) },
        onNavigateToForgotPassword = { navController.navigate(ForgotPasswordNavigation.route) },
        onSignUpSuccess = { recipient ->
            navController.navigate(OtpVerificationNavigation.createRoute(recipient))
        },
        onOtpValidated = {
            navController.navigate("main") {
                popUpTo("auth") { inclusive = true }
            }
        },
        onOtpFailed = { navController.popBackStack() },
        onResetPasswordSuccess = {
            navController.navigate(SignInNavigation.route) {
                popUpTo(WelcomeNavigation.destination) { inclusive = false }
            }
        }
    )
}