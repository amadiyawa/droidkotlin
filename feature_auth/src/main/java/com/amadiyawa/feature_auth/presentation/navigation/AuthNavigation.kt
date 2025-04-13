package com.amadiyawa.feature_auth.presentation.navigation

import com.amadiyawa.feature_base.presentation.navigation.AppNavigationDestination

object WelcomeNavigation : AppNavigationDestination {
    override val route = "auth_welcome"
    override val destination = "auth_welcome_destination"
}

object SignInNavigation : AppNavigationDestination {
    override val route = "auth_sign_in"
    override val destination = "auth_sign_in_destination"
}

object SignUpNavigation : AppNavigationDestination {
    override val route = "auth_sign_up"
    override val destination = "auth_sign_up_destination"
}

object OtpVerificationNavigation : AppNavigationDestination {
    private const val OTP_PARAM = "recipient"

    override val route = "auth_otp/{$OTP_PARAM}"
    override val destination = "auth_otp_destination"

    fun createRoute(recipient: String) = "auth_otp/$recipient"
}

object ForgotPasswordNavigation : AppNavigationDestination {
    override val route = "auth_forgot_password"
    override val destination = "auth_forgot_password_destination"
}

object ResetPasswordNavigation : AppNavigationDestination {
    override val route = "auth_reset_password"
    override val destination = "auth_reset_password_destination"
}
