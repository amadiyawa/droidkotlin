package com.amadiyawa.feature_auth.data.remote

import com.amadiyawa.feature_auth.data.datasource.api.model.SignInApiModel
import com.amadiyawa.feature_auth.data.datasource.api.model.SignUpApiModel
import com.amadiyawa.feature_auth.data.datasource.api.model.ForgotPasswordRequest
import com.amadiyawa.feature_auth.data.datasource.api.model.OtpVerificationRequest
import com.amadiyawa.feature_auth.data.datasource.api.model.ResetPasswordRequest
import com.amadiyawa.feature_auth.data.datasource.api.model.SignInRequest
import com.amadiyawa.feature_auth.data.datasource.api.model.SignUpRequest
import com.amadiyawa.feature_base.data.remote.model.SimpleApiResponse
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * Retrofit API service for authentication-related endpoints.
 */
interface AuthApiService {

    /**
     * Authenticates a user with their credentials.
     *
     * @param request The sign-in request containing identifier and password.
     * @return A DTO containing user details and access token on success.
     */
    @POST("auth/sign-in")
    suspend fun signIn(@Body request: SignInRequest): SignInApiModel

    /**
     * Registers a new user.
     *
     * @param request The sign-up request with necessary user info.
     * @return A DTO containing confirmation status or user profile.
     */
    @POST("auth/sign-up")
    suspend fun signUp(@Body request: SignUpRequest): SignUpApiModel

    /**
     * Verifies OTP for email or phone confirmation.
     *
     * @param request Contains recipient (email or phone) and otp code.
     * @return True if verification is successful.
     */
    @POST("auth/verify-otp")
    suspend fun verifyOtp(@Body request: OtpVerificationRequest): SimpleApiResponse

    /**
     * Requests a password reset (sends OTP).
     *
     * @param request Contains email or phone.
     * @return Status message or confirmation.
     */
    @POST("auth/forgot-password")
    suspend fun requestForgotPasswordOtp(@Body request: ForgotPasswordRequest): SimpleApiResponse

    /**
     * Resets the user's password after OTP confirmation.
     *
     * @param request Contains recipient, new password, and OTP.
     * @return Status of the reset.
     */
    @POST("auth/reset-password")
    suspend fun resetPassword(@Body request: ResetPasswordRequest): SimpleApiResponse
}