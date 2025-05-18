package com.amadiyawa.feature_auth.presentation.navigation

/**
 * Object containing the routes used in the authentication feature.
 *
 * This object defines the navigation routes for various authentication-related screens
 * in the application. It includes constants for static routes and functions for dynamic
 * routes that require parameters.
 */
object AuthRoutes {
    /**
     * Route for the welcome screen.
     */
    const val WELCOME = "welcome"

    /**
     * Route for the sign-in screen.
     */
    const val SIGN_IN = "signin"

    /**
     * Route for the sign-up screen.
     */
    const val SIGN_UP = "signup"

    /**
     * Route for the OTP verification screen.
     */
    const val OTP_VERIFICATION = "otp_verification"

    /**
     * Route for the forgot password screen.
     */
    const val FORGOT_PASSWORD = "forgot_password"

    /**
     * Route for the reset password screen.
     */
    const val RESET_PASSWORD = "reset_password"

    /**
     * Generates a dynamic route for the sign-in screen with a recipient parameter.
     *
     * @param recipient The recipient's identifier to be included in the route.
     * @return The dynamic route for the sign-in screen with the recipient parameter.
     */
    fun signInWithRecipient(recipient: String) = "$SIGN_IN?recipient=$recipient"

    /**
     * Generates a dynamic route for the OTP verification screen with data.
     *
     * @param verificationResultJson The JSON string containing verification data.
     * @return The dynamic route for the OTP verification screen with the data parameter.
     */
    fun otpVerificationWithData(verificationResultJson: String) = "$OTP_VERIFICATION?data=$verificationResultJson"

    /**
     * Generates a dynamic route for the reset password screen with a token.
     *
     * @param resetToken The token to be included in the route.
     * @return The dynamic route for the reset password screen with the token parameter.
     */
    fun resetPasswordWithToken(resetToken: String) = "$RESET_PASSWORD?token=$resetToken"
}