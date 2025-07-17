plugins {
    id("com.kira.feature")
}

android {
    namespace = "com.kira.ui.feature.editor"

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
    implementation(libs.view.material)

    // AAC
    implementation(libs.androidx.lifecycle.viewmodel)
    implementation(libs.androidx.lifecycle.runtime)
    implementation(libs.androidx.navigation)

    // Coroutines
    implementation(libs.coroutines.core)
    implementation(libs.coroutines.android)
    testImplementation(libs.coroutines.test)

    // DI
    implementation(libs.hilt)
    ksp(libs.hilt.compiler)

    // Modules
    implementation(project(":feature-editor:api"))
    implementation(project(":feature-explorer:api"))
    implementation(project(":feature-settings:api"))
    implementation(project(":feature-shortcuts:api"))
    implementation(project(":feature-themes:api"))
    implementation(project(":feature-fonts:api"))
    implementation(project(":common-core"))
    implementation(project(":common-ui"))

    // Tests
    testImplementation(libs.test.junit)
    testImplementation(libs.test.mockk)
    androidTestImplementation(libs.test.junit.ext)
    androidTestImplementation(libs.test.runner)
}