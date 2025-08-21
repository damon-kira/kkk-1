plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.bigdata.lib"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()

        consumerProguardFiles("proguard-rules.pro")
    }

    publishing {
        singleVariant("release") {
            withSourcesJar()
            withJavadocJar()
        }
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

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation(project(":sdks:util"))
    implementation(project(":sdks:cache"))

    api(libs.kotlin.stdlib)
    api(libs.androidx.core)
    api(libs.androidx.appcompat)

    api(libs.other.aes)

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