package com.amadiyawa.feature_auth.presentation.navigation

import android.net.Uri
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.amadiyawa.feature_auth.domain.model.VerificationResult
import com.amadiyawa.feature_auth.presentation.screen.authcheck.AuthCheckScreen
import com.amadiyawa.feature_auth.presentation.screen.forgotpassword.ForgotPasswordScreen
import com.amadiyawa.feature_auth.presentation.screen.otpverification.OtpVerificationScreen
import com.amadiyawa.feature_auth.presentation.screen.resetpassword.ResetPasswordScreen
import com.amadiyawa.feature_auth.presentation.screen.signin.SignInScreen
import com.amadiyawa.feature_auth.presentation.screen.signup.SignUpScreen
import com.amadiyawa.feature_auth.presentation.screen.welcome.WelcomeScreen
import com.amadiyawa.feature_base.presentation.navigation.AppRoutes
import kotlinx.serialization.json.Json
import timber.log.Timber

fun NavGraphBuilder.authGraph(navController: NavHostController) {
    navigation(
        startDestination = AuthRoutes.AUTH_CHECK,
        route = AppRoutes.AUTH_GRAPH
    ) {
        composable(AuthRoutes.AUTH_CHECK) {
            Timber.d("Checking authentication status")
            AuthCheckScreen(
                onAuthenticated = {
                    Timber.d("User already authenticated, navigating to main")
                    navController.navigateToMain()
                },
                onNotAuthenticated = {
                    Timber.d("User not authenticated, showing welcome")
                    navController.navigate(AuthRoutes.WELCOME) {
                        popUpTo(AuthRoutes.AUTH_CHECK) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(AuthRoutes.WELCOME) {
            Timber.d("Navigating to WelcomeScreen")
            WelcomeScreen(
                onSignIn = {
                    navController.navigate(AuthRoutes.SIGN_IN)
                },
                onSignUp = {
                    navController.navigate(AuthRoutes.SIGN_UP)
                }
            )
        }

        composable(
            route = "${AuthRoutes.SIGN_IN}?recipient={recipient}",
            arguments = listOf(navArgument("recipient") {
                type = NavType.StringType
                defaultValue = ""
            })
        ) { backStackEntry ->
            val recipient = backStackEntry.arguments?.getString("recipient") ?: ""
            Timber.d("Navigating to SignInScreen with recipient: $recipient")

            SignInScreen(
                defaultIdentifier = recipient,
                onSignInSuccess = {
                    Timber.d("Sign in successful, navigating to main")
                    navController.navigateToMain()
                },
                onForgotPassword = {
                    navController.navigate(AuthRoutes.FORGOT_PASSWORD)
                }
            )
        }

        composable(AuthRoutes.SIGN_UP) {
            Timber.d("Navigating to SignUpScreen")
            SignUpScreen(
                onSignUpSuccess = { verificationResult ->
                    Timber.d("Sign up successful, navigating to OTP verification")
                    val verificationJson = Json.encodeToString(VerificationResult.serializer(), verificationResult)
                    val encodedJson = Uri.encode(verificationJson)
                    navController.navigate(AuthRoutes.otpVerificationWithData(encodedJson)) {
                        popUpTo(AuthRoutes.SIGN_UP) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(
            route = "${AuthRoutes.OTP_VERIFICATION}?data={verificationResultJson}",
            arguments = listOf(navArgument("verificationResultJson") {
                type = NavType.StringType
            })
        ) { backStackEntry ->
            val json = backStackEntry.arguments?.getString("verificationResultJson")
            val data = json?.let { Json.decodeFromString<VerificationResult>(Uri.decode(it)) }

            Timber.d("Navigating to OtpVerificationScreen")

            if (data != null) {
                OtpVerificationScreen(
                    data = data,
                    onOtpValidated = {
                        Timber.d("OTP validated, navigating to main")
                        navController.navigateToMain()
                    },
                    onResetPassword = { resetToken ->
                        Timber.d("OTP for reset password, navigating to reset screen")
                        navController.navigate(AuthRoutes.resetPasswordWithToken(resetToken)) {
                            // Clear back to the entry point (Welcome or ForgotPassword)
                            popUpTo(AuthRoutes.WELCOME) { inclusive = false }
                            launchSingleTop = true
                        }
                    },
                    onCancel = {
                        navController.popBackStack()
                    }
                )
            }
        }

        composable(AuthRoutes.FORGOT_PASSWORD) {
            Timber.d("Navigating to ForgotPasswordScreen")
            ForgotPasswordScreen(
                onOtpSent = { verificationResult ->
                    Timber.d("OTP sent, navigating to verification")
                    val verificationJson = Json.encodeToString(VerificationResult.serializer(), verificationResult)
                    val encodedJson = Uri.encode(verificationJson)
                    navController.navigate(AuthRoutes.otpVerificationWithData(encodedJson)) {
                        popUpTo(AuthRoutes.FORGOT_PASSWORD) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(
            route = "${AuthRoutes.RESET_PASSWORD}?token={resetToken}",
            arguments = listOf(navArgument("resetToken") {
                type = NavType.StringType
            })
        ) { backStackEntry ->
            val resetToken = backStackEntry.arguments?.getString("resetToken") ?: ""
            Timber.d("Navigating to ResetPasswordScreen")

            ResetPasswordScreen(
                resetToken = resetToken,
                onSuccess = { recipient ->
                    Timber.d("Password reset successful, navigating to sign in")
                    navController.navigate(AuthRoutes.signInWithRecipient(recipient)) {
                        // Clear all auth screens and go back to welcome as the back destination
                        popUpTo(AuthRoutes.WELCOME) { inclusive = false }
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}