plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.hilt)
    alias(libs.plugins.kotlin.ksp)
}

android {
    namespace = "com.arezoonazer.player"
    compileSdk = libs.versions.compileSdk.get().toInt()

    buildFeatures {
        viewBinding = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    testOptions {
        unitTests {
            isReturnDefaultValues = true
        }
    }
}

dependencies {
    implementation(project(":common-base"))

    // Media3
    api(libs.media3.exoplayer)
    api(libs.media3.ui)
    api(libs.media3.exoplayer.hls)
    api(libs.media3.exoplayer.dash)

    // Hilt
    implementation(libs.hilt)
    ksp(libs.hilt.compiler)
}