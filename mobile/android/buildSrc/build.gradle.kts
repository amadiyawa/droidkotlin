plugins {
    `kotlin-dsl` // Enables the use of Kotlin to define Gradle scripts.
}

dependencies {
    implementation(plugin(libs.plugins.kotlin.android))
    implementation(plugin(libs.plugins.kotlin.compose))
    implementation(plugin(libs.plugins.kotlin.serialization))
    implementation(plugin(libs.plugins.kotlin.symbolProcessing))
    implementation(plugin(libs.plugins.android.application))
    implementation(plugin(libs.plugins.googleServices))
    implementation(plugin(libs.plugins.android.library))
    implementation(plugin(libs.plugins.spotless))
    implementation(plugin(libs.plugins.testLogger))
    implementation(plugin(libs.plugins.detekt))
    implementation(plugin(libs.plugins.junit5Android))
    implementation(plugin(libs.plugins.safeArgs))
}

kotlin {
    jvmToolchain(17)
}

/**
 * Converts a `Provider<PluginDependency>` to a `Provider<String>` representing the plugin coordinates.
 *
 * @param plugin The `Provider<PluginDependency>` to be converted.
 * @return A `Provider<String>` containing the plugin coordinates in the format "pluginId:pluginId.gradle.plugin:version".
 */
fun plugin(plugin: Provider<PluginDependency>) = plugin.map {
    "${it.pluginId}:${it.pluginId}.gradle.plugin:${it.version}"
}