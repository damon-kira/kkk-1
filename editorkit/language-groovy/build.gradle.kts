

plugins {
    id("com.kira.language")
    id("com.kira.publish")
}

publishModule {
    libraryGroup = "com.kira.ui"
    libraryArtifact = "language-groovy"
    libraryVersion = "2.9.0"
}

dependencies {

    // Modules
    api(project(":editorkit:language-base"))
}