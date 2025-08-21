plugins {
    id("com.kira.feature")
}

android {
    namespace = "com.kira.ui.feature.explorer.domain"

    buildFeatures {
        resValues = false
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
}

dependencies {

    // Core
    implementation(libs.kotlin.stdlib)
    implementation(libs.androidx.core)

    // Coroutines
    implementation(libs.coroutines.core)

    // Modules
    api(project(":filesystems:filesystem-base"))
}