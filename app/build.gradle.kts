import com.google.firebase.crashlytics.buildtools.gradle.CrashlyticsExtension
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.ksp)
    alias(libs.plugins.hilt)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.services)
    alias(libs.plugins.crashlytics)
}

android {
    namespace = "com.kira.learning"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.kira.learning.student"
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()

        multiDexEnabled = true

        // resConfigs("en") // 资源配置限制
        versionCode = 1
        versionName = "0.0.1"

        ndk {
            abiFilters += setOf("armeabi-v7a", "arm64-v8a")
        }

        manifestPlaceholders.apply {
            put("pkgName", "com.kira.learning.student")
        }
        setProperty("archivesBaseName", "Kira learning_v${versionName}_${time()}")

        // python {
        //     version = "3.10"
        //     buildPython = "python3"  // 自动检测系统Python3
        //     pip {
        //         install("numpy")
        //         install("colorama")
        //     }
        // }
    }

//    aaptOptions {
//        additionalParameters("--no-compress", "png")
//        additionalParameters("--no-compress")// 禁止压缩所有资源
//    }

    buildFeatures {
        viewBinding = true
        compose = true
        buildConfig = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }

    signingConfigs {
        create("release") {
            storeFile = file("keystore/kiraapac.jks")
            storePassword = "kiraapac2025"
            keyAlias = "kiraapac"
            keyPassword = "kiraapac2025"
        }
    }

    buildTypes {
        getByName("release") {  // 使用 getByName 而不是 create
            buildConfigField("String", "BASE_URL", "\"https://kira.learning.com\"")
            buildConfigField("String", "H5_URL", "\"https://kirah5.learning.com\"")
            buildConfigField("String", "DATA_URL", "\"httpsdata://kira.learning.com\"")
            buildConfigField("String", "APP_SECRET", "\"265F5D19C0EC03B1\"")
            buildConfigField("boolean", "APP_DEBUG", "false")
            isMinifyEnabled = true
            isShrinkResources = true
            configure<CrashlyticsExtension> {
                mappingFileUploadEnabled = true
            }
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")

        }
        getByName("debug") {
            buildConfigField("String", "BASE_URL", "\"https://core.staging.kira-learning.com/\"")
            buildConfigField("String", "H5_URL", "\"http://192.168.5.222:7755\"")
            buildConfigField(
                "String", "DATA_URL", "\"http://192.168.5.222:8083/XRmN4gV/N9Ed7VEoU\""
            )
            buildConfigField("String", "APP_SECRET", "\"83D763DA4B8B0734\"")
            buildConfigField("boolean", "APP_DEBUG", "true")
            isMinifyEnabled = false
            signingConfig = signingConfigs.getByName("debug")
            isDebuggable = true
            versionNameSuffix = ".alpha"
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    kotlinOptions {
        jvmTarget = "21"
    }

    lint {
        abortOnError = false
    }

    sourceSets {
        getByName("main") {
            jniLibs.srcDirs("libs")  // 修正为函数调用形式
        }
    }

    tasks.whenTaskAdded {
        if (name.contains("uploadCrashlyticsMappingFileRelease")) {
            enabled = false
        }
    }
}


fun time(): String {
    return LocalDateTime.now().format(DateTimeFormatter.ofPattern("MMddyyyyHHmm"))
}

dependencies {
    // Hilt
    implementation(libs.hilt)
    ksp(libs.hilt.compiler)

    // Room
    ksp(libs.androidx.room.compiler)

    // 图片处理
    implementation(libs.exifinterface)
    implementation(libs.cropper)

    // Firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.messaging)
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.crashlytics)

    // OCR
    implementation(libs.mlkit.text.recognition)

    // SuperEditor
    implementation(libs.androidx.splashscreen)
    implementation(libs.androidx.profileinstaller)
    implementation(libs.timber)
    implementation(libs.androidx.navigation)
    implementation(libs.hilt.workmanager)
    ksp(libs.hilt.android.compiler)

    implementation(libs.androidx.workmanager)

    implementation(project(":common-base"))
    implementation(project(":common-ui"))
    implementation(project(":sdks:util"))
    implementation(project(":module-player"))
    implementation(project(":feature-camera"))
    implementation(project(":feature-upload"))
    implementation(project(":compose-view-chat"))

    // Debug工具
    debugImplementation("com.squareup.leakcanary:leakcanary-android:2.14")

}
