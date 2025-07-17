plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.bigdata.lib"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()
//        versionCode = 1
//        versionName = "1.0"

        consumerProguardFiles("proguard-rules.pro")
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

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation(project(":sdks:util"))
    implementation(project(":sdks:cache"))

//    implementation(rootProject.extra["android.kotlin_stdlib"] as String)
//    implementation(rootProject.extra["android.core_ktx"] as String)
//    implementation(rootProject.extra["android.appcompat"] as String)
    api(libs.kotlin.stdlib)
    api(libs.androidx.core)
    api(libs.androidx.appcompat)

//    implementation(rootProject.extra["other.aes"] as String)
    api(libs.other.aes)

//    implementation(rootProject.extra["retrofit.gson"] as String)
//    implementation(rootProject.extra["retrofit.logging_interceptor"] as String)
//    implementation(rootProject.extra["retrofit.okhttp"] as String)
    api(libs.retrofit.gson)
    api(libs.retrofit.logging.interceptor)
    api(libs.retrofit.okhttp)

    // 定位SDK
    api(libs.location) {
        isChanging = true
        isTransitive = true
    }
}

apply(from = "../../maven_push.gradle")

group = "com.bigdata.lib"
version = "1.0.0"