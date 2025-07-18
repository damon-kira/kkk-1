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
    compileSdk = libs.versions.compileSdk.get().toInt()
    namespace = "com.kira.ui.editorkit"

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()

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