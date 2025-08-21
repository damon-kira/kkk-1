plugins {
    id("com.kira.filesystem")
}

dependencies {

    // Core
    implementation(libs.kotlin.stdlib)
    implementation(libs.bouncycastle)

    // Network
    implementation(libs.jsch)

    // Coroutines
    implementation(libs.coroutines.core)

    // Modules
    api(project(":filesystems:filesystem-base"))
}