package com.amadiyawa.droidkotlin

import android.app.Application
import com.amadiyawa.feature_base.featureBaseModule
import com.amadiyawa.feature_onboarding.featureOnboardingModule
import com.amadiyawa.feature_users.featureUserModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext
import timber.log.Timber

/**
 * Custom Application class for initializing global configurations.
 *
 * This class extends the [Application] class and is used to set up
 * dependencies and logging frameworks when the application is created.
 */
class MyApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        initKoin()
        initTimber()
    }

    private fun initKoin() {
        GlobalContext.startKoin {
            androidLogger()
            androidContext(this@MyApplication)

            modules(appModule)
            modules(featureBaseModule)
            modules(featureOnboardingModule)
            modules(featureUserModule)
        }
    }

    private fun initTimber() {
        Timber.plant(Timber.DebugTree())
    }
}