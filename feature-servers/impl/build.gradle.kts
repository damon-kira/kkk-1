plugins {
    id("com.kira.feature")
}

android {
    namespace = "com.kira.ui.feature.servers"

    buildFeatures {
        viewBinding = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    kotlinOptions {
        jvmTarget = "21"
    }
}

dependencies {

    // Core
    implementation(libs.kotlin.stdlib)
    implementation(libs.androidx.core)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.fragment)
    implementation(libs.timber)

    // UI
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.preferences)
    implementation(libs.view.material)

    // AAC
    implementation(libs.androidx.lifecycle.viewmodel)
    implementation(libs.androidx.lifecycle.runtime)
    implementation(libs.androidx.navigation)

    // Network
    implementation(libs.retrofit.gson)

    // Coroutines
    implementation(libs.coroutines.core)
    implementation(libs.coroutines.android)
    testImplementation(libs.coroutines.test)

    // DI
    implementation(libs.hilt)
    ksp(libs.hilt.compiler)
    implementation(libs.hilt.navigation)

    // Modules
    implementation(project(":feature-servers:api"))
    implementation(project(":common-core"))
    implementation(project(":common-ui"))

    implementation(project(":filesystems:filesystem-ftp"))
    implementation(project(":filesystems:filesystem-ftps"))
    implementation(project(":filesystems:filesystem-ftpes"))
    implementation(project(":filesystems:filesystem-sftp"))

    // Tests
    testImplementation(libs.test.junit)
    testImplementation(libs.test.mockk)
    androidTestImplementation(libs.test.junit.ext)
    androidTestImplementation(libs.test.runner)
}