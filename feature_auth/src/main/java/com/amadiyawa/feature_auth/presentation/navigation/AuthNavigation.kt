package com.amadiyawa.feature_auth.presentation.navigation

import com.amadiyawa.feature_auth.domain.model.VerificationResult
import com.amadiyawa.feature_base.presentation.navigation.AppNavigationDestination
import kotlinx.serialization.json.Json
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

object WelcomeNavigation : AppNavigationDestination {
    override val route = "auth_welcome"
    override val destination = "auth_welcome_destination"
}

object SignInNavigation : AppNavigationDestination {

    private const val RECIPIENT_PARAM = "recipient"

    override val route = "auth_sign_in/{$RECIPIENT_PARAM}"
    override val destination = "auth_sign_in_destination"

    fun createRoute(recipient: String): String {
        return "auth_sign_in/$recipient"
    }
}

object SignUpNavigation : AppNavigationDestination {
    override val route = "auth_sign_up"
    override val destination = "auth_sign_up_destination"
}

internal object OtpVerificationNavigation : AppNavigationDestination {

    private const val SIGN_UP_PARAM = "verificationResultJson"

    override val route = "auth_otp/{$SIGN_UP_PARAM}"
    override val destination = "auth_otp_destination"

    fun createRoute(data: VerificationResult): String {
        val json = Json.encodeToString(VerificationResult.serializer(), data)
        val encoded = URLEncoder.encode(json, StandardCharsets.UTF_8.toString())
        return "auth_otp/$encoded"
    }
}

object ForgotPasswordNavigation : AppNavigationDestination {
    override val route = "auth_forgot_password"
    override val destination = "auth_forgot_password_destination"
}

object ResetPasswordNavigation : AppNavigationDestination {
    override val route = "auth_reset_password"
    override val destination = "auth_reset_password_destination"
}
