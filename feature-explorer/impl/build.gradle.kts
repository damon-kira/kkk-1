plugins {
    id("com.kira.feature")
}

android {
    namespace = "com.kira.ui.feature.explorer"

    buildFeatures {
        viewBinding = true
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
    implementation(libs.view.constraintlayout)
    implementation(libs.view.swiperefreshlayout)
    implementation(libs.androidx.recyclerview.selection)
    implementation(libs.view.material)

    // AAC
    implementation(libs.androidx.lifecycle.viewmodel)
    implementation(libs.androidx.lifecycle.runtime)
    implementation(libs.androidx.navigation)
    implementation(libs.androidx.workmanager)

    // Network
    implementation(libs.retrofit.gson)

    // Coroutines
    implementation(libs.coroutines.core)
    implementation(libs.coroutines.android)
    testImplementation(libs.coroutines.test)

    // DI
    implementation(libs.hilt)
    ksp(libs.hilt.compiler)
    implementation(libs.hilt.workmanager)
    ksp(libs.hilt.android.compiler)

    // Modules
    implementation(project(":feature-explorer:api"))
    implementation(project(":feature-servers:api"))
    implementation(project(":feature-settings:api"))
    implementation(project(":common-core"))
    implementation(project(":common-ui"))

    implementation(project(":filesystems:filesystem-local"))
    implementation(project(":filesystems:filesystem-root"))
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