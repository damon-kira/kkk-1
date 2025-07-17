plugins {
    id("com.kira.feature")
}

android {
    namespace = "com.kira.ui.feature.themes.domain"

    buildFeatures {
        resValues = false
    }
}

dependencies {

    // Core
    implementation(libs.kotlin.stdlib)
    implementation(libs.androidx.core)

    // Modules
    api(project(":editorkit:editorkit"))
    api(project(":filesystems:filesystem-base"))
    api(project(":editorkit:language-base"))
}