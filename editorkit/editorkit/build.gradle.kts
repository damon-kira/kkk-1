

import com.kira.ui.BuildConst

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    id("com.kira.publish")
}

publishModule {
    libraryGroup = "com.kira.ui"
    libraryArtifact = "editorkit"
    libraryVersion = "2.9.0"
}

android {
    compileSdk = BuildConst.COMPILE_SDK
    namespace = "com.kira.ui.editorkit"

    defaultConfig {
        minSdk = BuildConst.MIN_SDK

        consumerProguardFiles("consumer-rules.pro")
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    sourceSets {
        named("main") {
            java.srcDir("src/main/kotlin")
        }
    }
    publishing {
        singleVariant("release") {
            withSourcesJar()
            withJavadocJar()
        }
    }
}

dependencies {

    // Core
    implementation(libs.kotlin.stdlib)
    implementation(libs.androidx.core)
    implementation(libs.androidx.vectordrawable)

    // Modules
    api(project(":editorkit:language-base"))
}