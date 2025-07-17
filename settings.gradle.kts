pluginManagement {
    includeBuild("build-logic")
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven {
            url = uri("${rootProject.projectDir.path}/repo")
        }
        maven {
            url = uri("https://maven.google.com") // Google's Maven repository
        }
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
        maven {
            url = uri("${rootProject.projectDir.path}/repo")
        }
        maven {
            url = uri("https://maven.google.com")
        }
        maven { url = uri("https://chaquo.com/maven") }
        maven { url = uri("https://jitpack.io") }
//        maven { url = uri("https://maven.aliyun.com/repository/public") }
    }
}

rootProject.name = "A1"
include(":app")
include(":common")
include(":camera")
//include(":datepicker")
//include(":analysis")

//if (isModule.toBoolean()) {
//    include(":sdks:bigdata")
//    include(":sdks:util")
//    include(":sdks:cache")
//}
include(":sdks:bigdata")
include(":sdks:util")
include(":sdks:cache")

//include(":rxpermission")
include(":stedit")
include(":player")
include(":window")
include(":image")
//include(":sdks:devoptions")
//include(":sdks:aes")
include(":richtext")
include(":codeview")


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
    ":filesystems:filesystem-root",
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
    ":editorkit:language-actionscript",
    ":editorkit:language-c",
    ":editorkit:language-cpp",
    ":editorkit:language-csharp",
    ":editorkit:language-css",
    ":editorkit:language-fortran",
    ":editorkit:language-go",
    ":editorkit:language-groovy",
    ":editorkit:language-html",
    ":editorkit:language-ini",
    ":editorkit:language-java",
    ":editorkit:language-javascript",
    ":editorkit:language-json",
    ":editorkit:language-julia",
    ":editorkit:language-kotlin",
    ":editorkit:language-latex",
    ":editorkit:language-lisp",
    ":editorkit:language-lua",
    ":editorkit:language-markdown",
    ":editorkit:language-php",
    ":editorkit:language-plaintext",
    ":editorkit:language-python",
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