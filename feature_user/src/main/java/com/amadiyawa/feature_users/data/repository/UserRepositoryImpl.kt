package com.amadiyawa.feature_users.data.repository

import android.util.Log
import com.amadiyawa.feature_base.data.retrofit.ApiResult
import com.amadiyawa.feature_users.data.datasource.api.service.UserRetrofitService
import com.amadiyawa.feature_users.data.datasource.database.UserDao
import com.amadiyawa.feature_users.domain.model.User
import com.amadiyawa.feature_base.domain.result.Result
import com.amadiyawa.feature_users.data.datasource.api.model.toDomainModel
import com.amadiyawa.feature_users.data.datasource.api.model.toEntityModel
import com.amadiyawa.feature_users.data.datasource.database.model.toDomainModel
import com.amadiyawa.feature_users.domain.repository.UserRepository
import timber.log.Timber

internal class UserRepositoryImpl(
    private val userRetrofitService: UserRetrofitService,
    private val userDao: UserDao
) : UserRepository {
    override suspend fun getUsers(page: Int, results: Int): Result<List<User>> =
        when (val apiResult = userRetrofitService.getUserListAsync(page, results)) {
            is ApiResult.Success -> {
                val users = apiResult
                    .data
                    .results
                    .also { usersApiModels ->
                        val usersEntityModels = usersApiModels.map { it.toEntityModel() }
                        userDao.insertUsers(usersEntityModels)
                    }
                    .map { it.toDomainModel() }

                Timber.tag("UserRepositoryImpl").d("getUsers: %s", users)

                Result.Success(users)
            }

            is ApiResult.Error -> {
                Result.Failure()
            }

            is ApiResult.Exception -> {
                Timber.e(apiResult.throwable)

                val users = userDao
                    .getAllUsers()
                    .map { it.toDomainModel() }

                Result.Success(users)
            }
        }

    override suspend fun getUserByUuid(uuid: String): Result<User> =
        try {
            val user = userDao
                .getUserByUuid(uuid)
                .toDomainModel()

            Result.Success(user)
        } catch (e: Exception) {
            Timber.e(e)
            Result.Failure()
        }

}