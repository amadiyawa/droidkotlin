plugins {
    id("local.library")
}

android {
    namespace = "com.amadiyawa.paygo.onboarding"
}

dependencies {
    api(projects.featureBase)

    testImplementation(libs.bundles.test)
    androidTestImplementation(libs.testng)

    testRuntimeOnly(libs.junitJupiterEngine)
}