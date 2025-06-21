plugins {
    id("local.library")
}

android {
    namespace = "com.droidkotlin.feature.profile"
}

dependencies {
    api(projects.featureBase)
}