plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
//    id("kotlin-kapt")
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    testOptions {
        unitTests {
            isReturnDefaultValues = true
        }
    }
}

dependencies {
    // Android 基础库
//    implementation(rootProject.extra["android.kotlin_stdlib"] as String)
//    implementation(rootProject.extra["android.core_ktx"] as String)
//    implementation(rootProject.extra["android.appcompat"] as String)
//    implementation(rootProject.extra["android.multidex"] as String)
//    implementation(rootProject.extra["android.activity_ktx"] as String)
//
//    // 视图组件
//    implementation(rootProject.extra["view.material"] as String)
//    implementation(rootProject.extra["view.constraintlayout"] as String)
//    implementation(rootProject.extra["view.swiperefreshlayout"] as String)
//
//    // Lifecycle
//    implementation(rootProject.extra["lifecycle.runtime"] as String)
//    implementation(rootProject.extra["lifecycle.livedataKtx"] as String)
//    implementation(rootProject.extra["lifecycle.viewmodelKtx"] as String)
    implementation(project(":common"))

    // Media3
    implementation(libs.media3.exoplayer)
    implementation(libs.media3.ui)
    implementation(libs.media3.exoplayer.hls)
    implementation(libs.media3.exoplayer.dash)

    // Hilt
    implementation(libs.hilt)
    ksp(libs.hilt.compiler)
}