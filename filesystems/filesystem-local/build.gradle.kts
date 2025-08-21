plugins {
    id("com.kira.filesystem")
}

dependencies {

    // Core
    implementation(libs.kotlin.stdlib)

    // Coroutines
    implementation(libs.coroutines.core)

    // Other
    implementation(libs.zip4j)
    implementation(libs.icu4j)

    // Modules
    api(project(":filesystems:filesystem-base"))
}