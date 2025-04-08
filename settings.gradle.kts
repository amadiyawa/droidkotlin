rootProject.name = "DroidKotlin"
include(
    ":app",
    ":feature_base",
    ":feature_onboarding",
    ":feature_user"
)

pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

@Suppress("UnstableApiUsage")
dependencyResolutionManagement {
    repositories {
        google()
        // Added for testing local Consist artifacts
        mavenLocal()
        mavenCentral()
    }
}

// Enables the feature preview for type-safe project accessors in Gradle.
// Type-safe project accessors provide a type-safe and refactoring-friendly way to access other projects.
// This feature is particularly useful in a multi-module project setup, where you often need to declare dependencies on other projects.
// By enabling this feature, you can access other projects in your settings.gradle.kts file in a type-safe manner.
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")