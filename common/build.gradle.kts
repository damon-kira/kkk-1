plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
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
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    kotlinOptions {
        jvmTarget = "21"
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
    api(libs.kotlin.stdlib)
    api(libs.androidx.core)
    api(libs.androidx.activity)
    api(libs.androidx.appcompat)
    api(libs.androidx.multidex)
    api(libs.coroutines.android)
    api(libs.androidx.fragment)
    api(libs.androidx.annotation)

    // View
    api(libs.view.viewpager2)
    api(libs.view.constraintlayout)
    api(libs.view.material)
    api(libs.view.recyclerview)
    api(libs.view.swiperefreshlayout)
    api(libs.view.svg)
    api(libs.view.gif)

    // Lifecycle
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
    api(platform(libs.compose.bom))
    api(libs.compose.ui)
    api(libs.compose.material)
    api(libs.compose.runtime)
    api(libs.compose.activity)
    api(libs.compose.material3)
    api(libs.compose.material.icons.extended)
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