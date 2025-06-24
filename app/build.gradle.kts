import com.android.build.api.dsl.ApplicationDefaultConfig
import java.io.FileInputStream
import java.util.Locale
import java.util.Properties

plugins {
    id("local.app")
}

// Load keystore properties
val keystorePropertiesFile = rootProject.file("keystore.properties")
val keystoreProperties = Properties()
if (keystorePropertiesFile.exists()) {
    keystoreProperties.load(FileInputStream(keystorePropertiesFile))
}

android {
    val catalogs = extensions.getByType<VersionCatalogsExtension>()
    val libs = catalogs.named("libs")

    namespace = "com.amadiyawa.droidkotlin"

    compileSdk = libs.findVersion("compileSdk").get().toString().toInt()

    defaultConfig {
        applicationId = "com.amadiyawa.droidkotlin"

        minSdk = libs.findVersion("minSdk").get().toString().toInt()
        targetSdk = libs.findVersion("targetSdk").get().toString().toInt()
        versionCode = 2
        versionName = "1.0.1" // SemVer (Major.Minor.Patch)

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        multiDexEnabled = true

        vectorDrawables {
            useSupportLibrary = true
        }

        buildConfigFieldFromGradleProperty("apiBaseUrl")
        buildConfigFieldFromGradleProperty("apiToken")
    }

    signingConfigs {
        create("release") {
            if (keystorePropertiesFile.exists()) {
                keyAlias = keystoreProperties["keyAlias"] as String
                keyPassword = keystoreProperties["keyPassword"] as String
                storeFile = file(keystoreProperties["storeFile"] as String)
                storePassword = keystoreProperties["storePassword"] as String
            }
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true

            ndk {
                debugSymbolLevel = "FULL"
            }

            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            if (keystorePropertiesFile.exists()) {
                signingConfig = signingConfigs.getByName("release")
            }
        }
    }

    buildFeatures {
        viewBinding = true
        buildConfig = true
        compose = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_11.toString()
    }

    testOptions {
        unitTests.isReturnDefaultValues = true
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    // Syntax utilizes Gradle TYPESAFE_PROJECT_ACCESSORS feature
    implementation(projects.featureOnboarding)
    implementation(projects.featureAuth)
    implementation(projects.featureBilling)
    implementation(projects.featureProfile)
}

/**
 * Sets a build config field from a Gradle project property.
 *
 * @param gradlePropertyName The name of the Gradle property to be used.
 * @throws IllegalStateException if the Gradle property is null.
 */
fun ApplicationDefaultConfig.buildConfigFieldFromGradleProperty(gradlePropertyName: String) {
    val propertyValue = project.properties[gradlePropertyName] as? String
    checkNotNull(propertyValue) { "Gradle property $gradlePropertyName is null" }

    val androidResourceName = "GRADLE_${gradlePropertyName.toSnakeCase()}".uppercase(Locale.getDefault())
    buildConfigField("String", androidResourceName, propertyValue)
}

/**
 * Converts a camelCase or PascalCase string to snake_case.
 *
 * @return The snake_case representation of the string.
 */
fun String.toSnakeCase() = this.split(Regex("(?=[A-Z])")).joinToString("_") {
    it.lowercase(Locale.getDefault())
}