plugins {
    id("com.kira.language")
    id("com.kira.publish")
}

publishModule {
    libraryGroup = "com.kira.ui"
    libraryArtifact = "language-base"
    libraryVersion = "2.9.0"
}

dependencies {

    // Core
    implementation(libs.kotlin.stdlib)
}