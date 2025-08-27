plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.composeCompiler)
    id("com.kira.stub")
}

android {
    compileSdk = libs.versions.compileSdk.get().toInt()
    namespace = "com.common.kira.ui"

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    kotlinOptions {
        jvmTarget = "21"
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    // Core
    implementation(libs.androidx.splashscreen)

    // UI
    implementation(libs.androidx.appcompat)
    implementation(libs.view.material)
    implementation(libs.colorpicker)
    implementation(libs.skydoves.colorpicker.compose)

    api(platform(libs.compose.bom))
    api(libs.compose.runtime)
    api(libs.compose.ui)
    api(libs.compose.foundation)
    api(libs.compose.material)
    api(libs.compose.material3)
    api(libs.compose.activity)
    api(libs.compose.uitooling.preview)
    api(libs.compose.material.icons.extended)
    api(libs.compose.navigation)
    api(libs.hilt.navigation.compose)
    debugApi(libs.compose.uitooling)
}