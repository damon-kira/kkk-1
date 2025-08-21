plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    id("com.kira.stub")
}

android {
    compileSdk = libs.versions.compileSdk.get().toInt()
    namespace = "com.kira.ui.uikit"

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
}