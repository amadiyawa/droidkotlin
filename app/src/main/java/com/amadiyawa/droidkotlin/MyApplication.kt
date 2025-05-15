package com.amadiyawa.droidkotlin

import android.app.Application
import com.amadiyawa.feature_auth.featureAuthModule
import com.amadiyawa.feature_base.featureBaseModule
import com.amadiyawa.feature_base.presentation.navigation.NavigationRegistry
import com.amadiyawa.feature_billing.BuildConfig
import com.amadiyawa.feature_billing.featureBillingModule
import com.amadiyawa.feature_onboarding.featureOnboardingModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext
import org.koin.core.qualifier.named
import org.koin.mp.KoinPlatformTools
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
            modules(featureAuthModule)
            modules(featureBillingModule)
        }

        // Trigger feature registrations
        try {
            // Correct way to get Koin instance
            val koin = KoinPlatformTools.defaultContext().get()

            // Execute all feature registrations with error handling
            try {
                koin.get<Boolean>(named("invoiceFeatureRegistration"))
                Timber.d("Invoice feature registered successfully")
            } catch (e: Exception) {
                Timber.e(e, "Failed to register invoice feature")
            }

            // Log registered features
            val registry = koin.get<NavigationRegistry>()
            Timber.d("All features registered, checking visible destinations")

            // Verify registrations
            verifyRegistrations(registry)

        } catch (e: Exception) {
            Timber.e(e, "Error during feature registration")
        }
    }

    private fun verifyRegistrations(registry: NavigationRegistry) {
        // Get a list of registered feature IDs to verify all expected features are present
        val registeredFeatures = registry.getRegisteredFeatureIds()
        Timber.d("Registered features: $registeredFeatures")

        // Verify expected features are registered
        val expectedFeatures = setOf("invoice", "user")
        val missingFeatures = expectedFeatures - registeredFeatures.toSet()

        if (missingFeatures.isNotEmpty()) {
            Timber.w("Missing expected features: $missingFeatures")
        }
    }

    private fun initTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        } else {
            // Consider adding a production-safe logging tree for crash reporting
            // Timber.plant(CrashReportingTree())
        }
    }
}