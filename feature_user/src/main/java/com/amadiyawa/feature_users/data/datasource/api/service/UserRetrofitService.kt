package com.amadiyawa.feature_users.data.datasource.api.service

import com.amadiyawa.feature_base.data.retrofit.ApiResult
import com.amadiyawa.feature_users.data.datasource.api.response.UserListResponse
import retrofit2.http.GET
import retrofit2.http.Query

internal interface UserRetrofitService {
    @GET("api/")
    suspend fun getUserListAsync(
        @Query("page") page: Int = 1,
        @Query("results") results: Int = 10,
        @Query("seed") seed: String = "weenect"
    ): ApiResult<UserListResponse>
}