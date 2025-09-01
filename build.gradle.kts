buildscript {
    dependencies {
//        classpath("com.google.gms:google-services:4.4.3")
//        classpath("com.google.firebase:firebase-crashlytics-gradle:3.0.4")
        // 确保其他插件也兼容（如Kotlin插件）
        classpath(libs.plugin.android)
        classpath(libs.plugin.kotlin)
    }
}

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.jetbrainsCompose) apply false
    alias(libs.plugins.composeCompiler) apply false
    alias(libs.plugins.navigation) apply false
    alias(libs.plugins.hilt) apply false
//    alias(libs.plugins.chaquoPython) apply false
    alias(libs.plugins.crashlytics) apply false
    alias(libs.plugins.kotlin.ksp) apply false
    alias(libs.plugins.services) apply false

}

apply(from = "./build_gradle/app.gradle")
