plugins {
    id("com.kira.feature")
}

android {
    namespace = "com.kira.ui.feature.changelog.domain"

    buildFeatures {
        resValues = false
    }
}

dependencies {

    // Core
    implementation(libs.kotlin.stdlib)
    implementation(libs.androidx.core)

    // Coroutines
    implementation(libs.coroutines.core)
}