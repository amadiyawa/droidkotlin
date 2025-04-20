package com.amadiyawa.feature_auth.presentation.navigation

import android.net.Uri
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.amadiyawa.feature_auth.domain.model.SignUp
import com.amadiyawa.feature_auth.presentation.screen.forgotpassword.ForgotPasswordScreen
import com.amadiyawa.feature_auth.presentation.screen.otpverification.OtpVerificationScreen
import com.amadiyawa.feature_auth.presentation.screen.resetpassword.ResetPasswordScreen
import com.amadiyawa.feature_auth.presentation.screen.signin.SignInScreen
import com.amadiyawa.feature_auth.presentation.screen.signup.SignUpScreen
import com.amadiyawa.feature_auth.presentation.screen.welcome.WelcomeScreen
import kotlinx.serialization.json.Json

fun NavGraphBuilder.authGraph(
    onSignIn: () -> Unit,
    onNavigateToSignUp: () -> Unit,
    onNavigateToForgotPassword: () -> Unit,
    onSignUpSuccess: (signUp: SignUp) -> Unit,
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
            arguments = listOf(navArgument("signUpJson") { type = NavType.StringType })
        ) { backStackEntry ->
            val json = backStackEntry.arguments?.getString("signUpJson")
            val signUp = json?.let { Json.decodeFromString<SignUp>(Uri.decode(it)) }

            if (signUp != null) {
                OtpVerificationScreen(
                    signUp = signUp,
                    onOtpValidated = onOtpValidated,
                    onCancel = onOtpFailed
                )
            } else {
                // Handle null case: maybe navigate back or show error
            }
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
        onSignUpSuccess = { signUp: SignUp ->
            navController.navigate(OtpVerificationNavigation.createRoute(signUp)) {
                popUpTo(SignUpNavigation.route) { inclusive = true }
                launchSingleTop = true
            }
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