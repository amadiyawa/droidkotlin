plugins {
    id("local.library")
}

android {
    namespace = "com.droidkotlin.onboarding"
}

dependencies {
    api(projects.featureBase)
}