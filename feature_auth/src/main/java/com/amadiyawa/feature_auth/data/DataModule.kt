package com.amadiyawa.feature_auth.data

import com.amadiyawa.feature_auth.data.source.remote.AuthApiService
import com.amadiyawa.feature_auth.data.repository.SimulatedAuthRepository
import com.amadiyawa.feature_auth.domain.repository.AuthRepository
import org.koin.dsl.module
import retrofit2.Retrofit

internal val dataModule = module {
    single<AuthRepository> { SimulatedAuthRepository() }
    single { get<Retrofit>().create(AuthApiService::class.java)}
}