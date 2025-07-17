plugins {
    id("com.kira.filesystem")
}

dependencies {

    // Core
    implementation(libs.kotlin.stdlib)

    // Network
    implementation(libs.ftpclient)

    // Coroutines
    implementation(libs.coroutines.core)

    // Modules
    api(project(":filesystems:filesystem-base"))
}