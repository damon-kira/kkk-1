

package com.kira.ui

import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.tasks.SourceSetContainer
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

class FilesystemModulePlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            val androidSpecific = plugins.hasPlugin("com.android.library")
            with(pluginManager) {
                if (androidSpecific) {
                    apply("org.jetbrains.kotlin.android")
                } else {
                    apply("java-library")
                    apply("org.jetbrains.kotlin.jvm")
                }
            }

            if (androidSpecific) {
                configure<LibraryExtension> {
                    compileSdk = BuildConst.COMPILE_SDK

                    defaultConfig {
                        minSdk = BuildConst.MIN_SDK

                        consumerProguardFiles("consumer-rules.pro")
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
                    sourceSets {
                        named("main") {
                            java.srcDir("src/main/kotlin")
                        }
                    }
                    buildFeatures {
                        buildConfig = true
                    }
                }
            } else {
                configure<JavaPluginExtension> {
                    sourceCompatibility = JavaVersion.VERSION_17
                    targetCompatibility = JavaVersion.VERSION_17
                }
                configure<SourceSetContainer> {
                    named("main") {
                        java.srcDir("src/main/kotlin")
                    }
                }
            }
        }
    }
}