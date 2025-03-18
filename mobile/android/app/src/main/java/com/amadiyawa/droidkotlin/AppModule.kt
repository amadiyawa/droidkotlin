package com.amadiyawa.droidkotlin

import com.amadiyawa.feature_base.data.retrofit.ApiResultAdapterFactory
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import timber.log.Timber

val appModule = module {
    single {
        HttpLoggingInterceptor { message ->
            Timber.d("Http: $message")
        }.apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    single {
        OkHttpClient.Builder()
            .addInterceptor(get<HttpLoggingInterceptor>())
            .build()
    }

    single {
        val contentType = "application/json".toMediaType()

        val json = Json {
            // By default Kotlin serialization will serialize all of the keys present in JSON object and throw an
            // exception if given key is not present in the Kotlin class. This flag allows to ignore JSON fields
            ignoreUnknownKeys = true
        }

        Retrofit.Builder()
            .baseUrl(BuildConfig.GRADLE_API_BASE_URL)
            .client(get())
            .addConverterFactory(json.asConverterFactory(contentType))
            .addCallAdapterFactory(ApiResultAdapterFactory())
            .build()
    }
}