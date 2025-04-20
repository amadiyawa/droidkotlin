package com.amadiyawa.feature_auth.data.repository

import com.amadiyawa.feature_auth.data.mapper.toDomainModel
import com.amadiyawa.feature_auth.data.remote.AuthApiService
import com.amadiyawa.feature_auth.domain.model.SignIn
import com.amadiyawa.feature_auth.data.datasource.api.model.SignInRequest
import com.amadiyawa.feature_auth.data.datasource.api.model.SignUpRequest
import com.amadiyawa.feature_auth.domain.model.SignUp
import com.amadiyawa.feature_auth.domain.repository.AuthRepository
import com.amadiyawa.feature_base.domain.result.OperationResult
import kotlinx.coroutines.CancellationException
import retrofit2.HttpException
import java.io.IOException

internal class AuthRepositoryImpl(
    private val api: AuthApiService
) : AuthRepository {

    override suspend fun signIn(signInRequest: SignInRequest): OperationResult<SignIn> {
        return try {
            val response = api.signIn(signInRequest)
            OperationResult.Success(response.toDomainModel())
        } catch (e: HttpException) {
            OperationResult.Failure(code = e.code(), message = e.message())
        } catch (e: IOException) {
            OperationResult.Error(e)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            OperationResult.Error(e)
        }
    }

    override suspend fun signUp(signUpRequest: SignUpRequest): OperationResult<SignUp> {
        return try {
            val response = api.signUp(signUpRequest)
            OperationResult.Success(response.toDomainModel())
        } catch (e: HttpException) {
            OperationResult.Failure(code = e.code(), message = e.message())
        } catch (e: IOException) {
            OperationResult.Error(e)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            OperationResult.Error(e)
        }
    }
}