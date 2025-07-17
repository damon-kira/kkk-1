import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `kotlin-dsl`
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

tasks.withType<KotlinCompile>().configureEach {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_17)
    }
}

dependencies {
    compileOnly(libs.plugin.android)
    compileOnly(libs.plugin.kotlin)
}

gradlePlugin {
    plugins {
        register("com.kira.application") {
            id = "com.kira.application"
            implementationClass = "com.kira.ui.ApplicationModulePlugin"
        }
        register("com.kira.benchmark") {
            id = "com.kira.benchmark"
            implementationClass = "com.kira.ui.BenchmarkModulePlugin"
        }
        register("com.kira.feature") {
            id = "com.kira.feature"
            implementationClass = "com.kira.ui.FeatureModulePlugin"
        }
        register("com.kira.filesystem") {
            id = "com.kira.filesystem"
            implementationClass = "com.kira.ui.FilesystemModulePlugin"
        }
        register("com.kira.language") {
            id = "com.kira.language"
            implementationClass = "com.kira.ui.LanguageModulePlugin"
        }
        register("com.kira.publish") {
            id = "com.kira.publish"
            implementationClass = "com.kira.ui.PublishModulePlugin"
        }
        register("com.kira.stub") {
            id = "com.kira.stub"
            implementationClass = "com.kira.ui.StubModulePlugin"
        }
    }
}