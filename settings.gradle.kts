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

include(":compose-view-stedit")
include(":module-player")
include(":view-window")
include(":view-image")
include(":view-richtext")
//include(":sdks:devoptions")
//include(":codeview")

include(
    ":common-core",
    ":common-ui",
    ":feature-changelog:api",
    ":feature-changelog:impl",
    ":feature-editor:api",
    ":feature-editor:impl",
    ":feature-explorer:api",
    ":feature-explorer:impl",
    ":feature-fonts:api",
    ":feature-fonts:impl",
    ":feature-servers:api",
    ":feature-servers:impl",
    ":feature-settings:api",
    ":feature-settings:impl",
    ":feature-shortcuts:api",
    ":feature-shortcuts:impl",
    ":feature-themes:api",
    ":feature-themes:impl",
)

include(
    ":filesystems:filesystem-base",
    ":filesystems:filesystem-local",
//    ":filesystems:filesystem-root",
    ":filesystems:filesystem-ftp",
    ":filesystems:filesystem-ftps",
    ":filesystems:filesystem-ftpes",
    ":filesystems:filesystem-sftp",
    // TODO ":filesystems:filesystem-dropbox",
    // TODO ":filesystems:filesystem-googledrive",
)

include(
    ":editorkit:editorkit",
    ":editorkit:language-base",
    ":editorkit:language-cpp",
    ":editorkit:language-java",
    ":editorkit:language-javascript",
    ":editorkit:language-kotlin",
    ":editorkit:language-php",
    ":editorkit:language-python",
    ":editorkit:language-html",
    ":editorkit:language-actionscript",
    ":editorkit:language-css",
    ":editorkit:language-c",
    ":editorkit:language-csharp",
    ":editorkit:language-fortran",
    ":editorkit:language-go",
    ":editorkit:language-json",
    ":editorkit:language-groovy",
    ":editorkit:language-ini",
    ":editorkit:language-julia",
    ":editorkit:language-latex",
    ":editorkit:language-lisp",
    ":editorkit:language-lua",
    ":editorkit:language-markdown",
    ":editorkit:language-plaintext",
    ":editorkit:language-ruby",
    ":editorkit:language-rust",
    ":editorkit:language-shell",
    ":editorkit:language-smali",
    ":editorkit:language-sql",
    ":editorkit:language-toml",
    ":editorkit:language-typescript",
    ":editorkit:language-visualbasic",
    ":editorkit:language-xml",
    ":editorkit:language-yaml",
)
include(":feature-upload")
include(":compose-view-chat")
