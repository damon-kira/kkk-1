plugins {
    alias(libs.plugins.android.library)
    id("com.kira.filesystem")
}

android {
    namespace = "com.kira.ui.filesystem.root"
}

dependencies {

    // Core
    implementation(libs.kotlin.stdlib)

    // Coroutines
    implementation(libs.coroutines.core)

    // Other
    implementation(libs.icu4j)
    implementation(libs.superuser)

    // Modules
    api(project(":filesystems:filesystem-base"))
}