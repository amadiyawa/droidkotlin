package com.amadiyawa.feature_auth.presentation.navigation

import android.net.Uri
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.amadiyawa.feature_auth.domain.model.VerificationResult
import com.amadiyawa.feature_auth.domain.util.OtpPurpose
import com.amadiyawa.feature_auth.presentation.screen.forgotpassword.ForgotPasswordScreen
import com.amadiyawa.feature_auth.presentation.screen.otpverification.OtpVerificationScreen
import com.amadiyawa.feature_auth.presentation.screen.resetpassword.ResetPasswordScreen
import com.amadiyawa.feature_auth.presentation.screen.signin.SignInScreen
import com.amadiyawa.feature_auth.presentation.screen.signup.SignUpScreen
import com.amadiyawa.feature_auth.presentation.screen.welcome.WelcomeScreen
import kotlinx.serialization.json.Json

internal data class AuthGraphCallbacks(
    val onSignIn: () -> Unit,
    val onNavigateToSignUp: () -> Unit,
    val onNavigateToForgotPassword: () -> Unit,
    val onSignInSuccess: () -> Unit,
    val onSignUpSuccess: (data: VerificationResult) -> Unit,
    val onOtpSentFromReset: (data: VerificationResult) -> Unit,
    val onOtpValidated: (data: VerificationResult) -> Unit,
    val onOtpFailed: () -> Unit,
    val onResetPasswordSuccess: (recipient: String) -> Unit
)

internal fun NavGraphBuilder.authGraph(callbacks: AuthGraphCallbacks) {
    navigation(
        startDestination = WelcomeNavigation.destination,
        route = "auth"
    ) {
        composable(WelcomeNavigation.destination) {
            WelcomeScreen(
                onSignIn = callbacks.onSignIn,
                onSignUp = callbacks.onNavigateToSignUp
            )
        }

        composable(
            route = SignInNavigation.route,
            arguments = listOf(navArgument("recipient") { type = NavType.StringType })
        ) { backStackEntry ->
            val recipient = backStackEntry.arguments?.getString("recipient")

            SignInScreen(
                defaultIdentifier = recipient,
                onSignInSuccess = callbacks.onSignInSuccess,
                onForgotPassword = callbacks.onNavigateToForgotPassword
            )
        }

        composable(SignUpNavigation.route) {
            SignUpScreen(
                onSignUpSuccess = callbacks.onSignUpSuccess
            )
        }

        composable(
            route = OtpVerificationNavigation.route,
            arguments = listOf(navArgument("verificationResultJson") { type = NavType.StringType })
        ) { backStackEntry ->
            val json = backStackEntry.arguments?.getString("verificationResultJson")
            val data = json?.let { Json.decodeFromString<VerificationResult>(Uri.decode(it)) }

            if (data != null) {
                OtpVerificationScreen(
                    data = data,
                    onOtpValidated = callbacks.onOtpValidated,
                    onCancel = callbacks.onOtpFailed
                )
            }
        }

        composable(ForgotPasswordNavigation.route) {
            ForgotPasswordScreen(
                onOtpSent = callbacks.onOtpSentFromReset
            )
        }

        composable(ResetPasswordNavigation.route) {
            ResetPasswordScreen(
                onSuccess = callbacks.onResetPasswordSuccess
            )
        }
    }
}

fun NavGraphBuilder.authGraph(navController: NavHostController) {
    val callbacks = AuthGraphCallbacks(
        onSignIn = { navController.navigate(SignInNavigation.createRoute("")) },
        onNavigateToSignUp = { navController.navigate(SignUpNavigation.route) },
        onNavigateToForgotPassword = { navController.navigate(ForgotPasswordNavigation.route) },
        onSignInSuccess = {
            navController.navigate("main") {
                popUpTo("auth") { inclusive = true }
                launchSingleTop = true
            }
        },
        onSignUpSuccess = { data ->
            navController.navigate(OtpVerificationNavigation.createRoute(data)) {
                popUpTo(SignUpNavigation.route) { inclusive = true }
                launchSingleTop = true
            }
        },
        onOtpSentFromReset = { data ->
            navController.navigate(OtpVerificationNavigation.createRoute(data)) {
                popUpTo(ForgotPasswordNavigation.route) { inclusive = true }
                launchSingleTop = true
            }
        },
        onOtpValidated = { signUp ->
            if (signUp.purpose == OtpPurpose.SIGN_UP) {
                navController.navigate("main") {
                    popUpTo("auth") { inclusive = true }
                    launchSingleTop = true
                }
            } else {
                navController.navigate(ResetPasswordNavigation.route) {
                    popUpTo(OtpVerificationNavigation.route) { inclusive = true }
                    launchSingleTop = true
                }
            }
        },
        onOtpFailed = { navController.popBackStack() },
        onResetPasswordSuccess = { recipient ->
            navController.navigate(SignInNavigation.createRoute(recipient)) {
                popUpTo(ResetPasswordNavigation.route) { inclusive = true }
                launchSingleTop = true
            }
        }
    )

    authGraph(callbacks)
}