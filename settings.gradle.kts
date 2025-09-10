pluginManagement {
    includeBuild("build-logic")
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven { url = uri("${rootProject.projectDir.path}/repo") }
        maven { url = uri("https://maven.google.com") }
        maven { url = uri("https://chaquo.com/maven") }
        maven { url = uri("https://jitpack.io") }
//        maven { url = uri("https://maven.aliyun.com/repository/public") }
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = uri("${rootProject.projectDir.path}/repo") }
        maven { url = uri("https://maven.google.com") }
        maven { url = uri("https://chaquo.com/maven") }
        maven { url = uri("https://jitpack.io") }
//        maven { url = uri("https://maven.aliyun.com/repository/public") }
    }
}

rootProject.name = "A1"
include(":app")
include(":common-base")
include(":feature-camera")
include(":sdks:util")
include(":sdks:cache")
include(":module-player")
include(":common-ui")
include(":feature-upload")
include(":compose-view-chat")
