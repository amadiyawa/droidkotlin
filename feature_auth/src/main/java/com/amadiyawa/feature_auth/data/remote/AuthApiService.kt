package com.amadiyawa.feature_auth.data.remote

import com.amadiyawa.feature_auth.data.datasource.api.model.SignInApiModel
import com.amadiyawa.feature_auth.domain.model.SignInRequest
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
}