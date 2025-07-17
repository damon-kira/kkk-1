import com.kira.ui.BuildConst

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    id("com.kira.stub")
}

android {
    compileSdk = BuildConst.COMPILE_SDK
    namespace = "com.kira.ui.uikit"

    defaultConfig {
        minSdk = BuildConst.MIN_SDK
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
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