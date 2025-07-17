package com.kira.ui

import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.util.Properties

class ApplicationModulePlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.application")
                apply("org.jetbrains.kotlin.android")
                apply("com.google.devtools.ksp")
                apply("dagger.hilt.android.plugin")
            }

            configure<BaseAppModuleExtension> {
                compileSdk = BuildConst.COMPILE_SDK

                defaultConfig {
                    minSdk = BuildConst.MIN_SDK
                    targetSdk = BuildConst.TARGET_SDK

                    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                }
                setFlavorDimensions(listOf("store"))
                productFlavors {
                    create("googlePlay") { dimension = "store" }
                    create("fdroid") { dimension = "store" }
                }
                val properties = Properties().apply {
                    val localFile = rootProject.file("local.properties")
                    if (localFile.exists()) {
                        load(localFile.inputStream())
                    }
                }
                signingConfigs {
                    create("release") {
                        storeFile = file("${properties["KEYSTORE_PATH"]}")
                        storePassword = "${properties["KEYSTORE_PASSWORD"]}"
                        keyAlias = "${properties["KEY_ALIAS"]}"
                        keyPassword = "${properties["KEY_PASSWORD"]}"
                    }
                }
                buildTypes {
                    release {
                        signingConfig = signingConfigs.getByName("release")
                        isMinifyEnabled = true
                        isShrinkResources = false
                        proguardFiles(
                            getDefaultProguardFile("proguard-android-optimize.txt"),
                            "proguard-rules.pro"
                        )
                    }
                    create("benchmark") {
                        initWith(buildTypes.getByName("release"))
                        signingConfig = signingConfigs.getByName("debug")
                        matchingFallbacks += listOf("release")
                        isMinifyEnabled = false
                        isDebuggable = false
                    }
                }
                compileOptions {
                    sourceCompatibility = JavaVersion.VERSION_17
                    targetCompatibility = JavaVersion.VERSION_17
                }
                tasks.withType<KotlinCompile>().configureEach {
                    kotlinOptions {
                        jvmTarget = "17"
                    }
                }
                sourceSets.configureEach {
                    java.srcDirs("src/${this.name}/kotlin")
                }
            }
        }
    }
}