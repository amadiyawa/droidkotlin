package com.amadiyawa.droidkotlin

import android.app.Application
import com.amadiyawa.feature_base.featureBaseModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext
import timber.log.Timber

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
        }
    }

    private fun initTimber() {
        Timber.plant(Timber.DebugTree())
    }
}