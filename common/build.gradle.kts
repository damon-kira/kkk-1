plugins {
//    alias(libs.plugins.android.library)
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
//    id("kotlin-kapt")
    alias(libs.plugins.kotlin.ksp)
    alias(libs.plugins.hilt)
}

android {
    namespace = "com.common.lib"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()
        consumerProguardFiles("proguard-rules.pro")
    }

    buildFeatures {
        viewBinding = true
        buildConfig = true
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }
}

dependencies {
    // 项目依赖
    api(project(":sdks:util"))
    api(project(":sdks:cache"))

    // Android Base
//    api(rootProject.extra["android.kotlin_stdlib"] as String)
//    api(rootProject.extra["android.core_ktx"] as String)
//    api(rootProject.extra["android.activity_ktx"] as String)
//    api(rootProject.extra["android.appcompat"] as String)
//    api(rootProject.extra["android.multidex"] as String)
//    api(rootProject.extra["android.coroutines_android"] as String)
//    api(rootProject.extra["android.fragment"] as String)
//    api(rootProject.extra["android.annotation"] as String)
    api(libs.kotlin.stdlib)
    api(libs.androidx.core)
    api(libs.androidx.activity)
    api(libs.androidx.appcompat)
    api(libs.androidx.multidex)
    api(libs.coroutines.android)
    api(libs.androidx.fragment)
    api(libs.androidx.annotation)

    // View
//    api(rootProject.extra["view.viewpager2"] as String)
//    api(rootProject.extra["view.constraintlayout"] as String)
//    api(rootProject.extra["view.material"] as String)
//    api(rootProject.extra["view.recyclerview"] as String)
//    api(rootProject.extra["view.swiperefreshlayout"] as String)
//    api(rootProject.extra["view.svg"] as String)
//    api(rootProject.extra["view.gif"] as String)
    api(libs.view.viewpager2)
    api(libs.view.constraintlayout)
    api(libs.view.material)
    api(libs.view.recyclerview)
    api(libs.view.swiperefreshlayout)
    api(libs.view.svg)
    api(libs.view.gif)

    // Lifecycle
//    api(rootProject.extra["lifecycle.runtime"] as String)
//    api(rootProject.extra["lifecycle.livedataKtx"] as String)
//    api(rootProject.extra["lifecycle.viewmodelKtx"] as String)
    api(libs.androidx.lifecycle.viewmodel)
    api(libs.androidx.lifecycle.livedata)
    api(libs.androidx.lifecycle.runtime)

    // Guava
    api(libs.guava.android)
    api(libs.guava.coroutines)

    // Hilt
//    api(rootProject.extra["hilt.hilt_android"] as String)
//    kapt(rootProject.extra["hilt.hilt_compiler_android"] as String)
    implementation(libs.hilt)
    ksp(libs.hilt.compiler)

    // RxJava
//    api(rootProject.extra["rxjava.rxandroid"] as String)
//    api(rootProject.extra["rxjava.rxjava"] as String)
    api(libs.rxjava)
    api(libs.rxjava.android)

    // Retrofit
    api(libs.retrofit)
    api(libs.retrofit.converter.gson)
    api(libs.retrofit.rxjava)
    api(libs.retrofit.gson)
    api(libs.retrofit.logging.interceptor)
    api(libs.retrofit.okhttp)
    api(libs.retrofit.android)

    // Image
    api(libs.image.glide)
    api(libs.image.egloo)

    // 适配
    api(libs.autosize)

    // Compose
//    api(platform(rootProject.extra["compose.bom"] as String))
//    api(rootProject.extra["compose.compose_ui"] as String)
//    api(rootProject.extra["compose.compose_material"] as String)
//    api(rootProject.extra["compose.compose_runtime"] as String)
//    api(rootProject.extra["compose.compose_activity"] as String)
//    debugApi(rootProject.extra["compose.compose_uitooling"] as String)
    api(platform(libs.compose.bom))
    api(libs.compose.ui)
    api(libs.compose.material)
    api(libs.compose.runtime)
    api(libs.compose.activity)
    debugApi(libs.compose.uitooling)

    // Markwon
    api(libs.markwon.core)
    api(libs.markwon.image)
    api(libs.markwon.image.glide)

    // 本地Maven
//    api(rootProject.extra["other.aes"] as String)
//    debugApi(rootProject.extra["other.devoptions"] as String)
    api(libs.other.aes)
    debugApi(libs.other.devoptions)
}