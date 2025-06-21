plugins {
    id("local.library")
}

android {
    namespace = "com.amadiyawa.droidkotlin.base"
}

dependencies {
    // Kotlin core
    api(libs.kotlin.reflect)
    api(libs.kotlinx.coroutines.android)
    api(libs.kotlinx.serialization.json)

    // Android core
    api(libs.androidx.core.ktx)
    api(libs.androidx.appcompat)
    api(libs.androidx.activity.compose)

    // Lifecycle and ViewModel
    api(libs.bundles.androidx.lifecycle)

    /// Jetpack compose
    api(platform(libs.androidx.compose.bom))
    api(libs.bundles.compose)
    api(libs.bundles.compose.material)
    api(libs.androidx.navigation.compose)

    // Material design
    api(libs.material)

    // Room database
    api(libs.bundles.androidx.room)
    kapt(libs.androidx.room.compiler)

    // Dependency injection
    api(platform(libs.koin.bom))
    api(libs.bundles.koin)

    // Networking
    api(libs.bundles.networking)

    // Data storage
    api(libs.androidx.datastore.preferences)

    //Utilities
    api(libs.timber)
    api(libs.libphonenumber)
    api(libs.system.ui.controller)

    // Firebase
    api(platform(libs.firebase.bom))
    api(libs.firebase.analytics)
    api(libs.firebase.firestore)

    // Testing
    api(libs.junit5.api)
    api(libs.junit5.params)
    api(libs.junit5.engine)
    api(libs.mockk)
    api(libs.kotlinx.coroutines.test)
    api(libs.androidx.compose.ui.test.junit4)
    api(libs.androidx.compose.ui.test.manifest)
    api(libs.androidx.compose.ui.tooling)
    api(libs.konsist)
}