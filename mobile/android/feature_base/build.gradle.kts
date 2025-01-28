plugins {
    id("local.library")
}

android {
    namespace = "com.amadiyawa.paygo.base"
}

dependencies {

    api(libs.kotlin)
    api(libs.androidx.core.ktx)
    api(libs.timber)
    api(libs.androidx.appcompat)
    api(libs.coroutines)
    api(libs.material)
    api(libs.androidx.activity.compose)
    api(libs.navigationCompose)
    api(platform(libs.androidx.compose.bom))
    api(libs.androidx.material3)
    api(libs.androidx.material3.android)
    api(libs.material3WindowSize)
    api(platform(libs.koin.bom))
    api(libs.koin.core)
    api(libs.koin.compose)
    api(libs.koin.androidx.compose)
    api(libs.koin.androidx.compose.navigation)
    api(libs.bundles.retrofit)
    api(libs.bundles.compose)
    api(libs.bundles.lifecycle)
    api(libs.bundles.room)
    api(libs.hilt)
    api(libs.datastorePreferences)
    kapt(libs.hiltCompiler)

    // Import the Firebase BoM
    api(platform(libs.firebaseBom))
    // Declare the dependency for the Firebase SDK for Google Analytics
    api(libs.firebaseAnalytics)
    api(libs.firebaseFirestore)


    testImplementation(libs.bundles.test)

    testRuntimeOnly(libs.junitJupiterEngine)
}