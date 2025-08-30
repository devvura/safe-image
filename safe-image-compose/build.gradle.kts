import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.vanniktech.mavenPublish)
}

group = "io.github.devvura"
version = "1.0.0"

kotlin {
    androidTarget {
        publishLibraryVariants("release")
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }
    sourceSets.commonMain.dependencies {
        implementation(libs.coil.compose)
        implementation(libs.coil.network.ktor)
    }
    sourceSets.commonTest.dependencies {
        implementation(libs.kotlin.test)
    }

    sourceSets.androidMain.dependencies {
        implementation(libs.ktor.client.android)
        implementation(libs.androidx.core.ktx)
        implementation(libs.tensorflow.lite.metadata)
        implementation(libs.tensorflow.lite.support)
    }
}

android {
    namespace = "io.github.devvura.safeimage.library"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

mavenPublishing {
    publishToMavenCentral()

    signAllPublications()

    coordinates(group.toString(), "safe-image-compose", version.toString())

    pom {
        name = "Safe Image Compose"
        description = "A Jetpack Compose library to load images safely with NSFW detection and blurring."
        inceptionYear = "2025"
        url = "https://github.com/devvura/safe-image/"
        licenses {
            license {
                name = "The Apache License, Version 2.0"
                url = "http://www.apache.org/licenses/LICENSE-2.0.txt"
                distribution = "http://www.apache.org/licenses/LICENSE-2.0.txt"
            }
        }
        developers {
            developer {
                id = "abdulrahmankhattab"
                name = "Abdulrahman Khattab"
                url = "https://github.com/Abdulrahman-Khattab"
            }
            developer {
                id = "faresmohamed"
                name = "Fares Mohamed"
                url = "https://github.com/FaresM0hamed"
            }
            developer {
                id = "malakraef"
                name = "Malak Raef"
                url = "https://github.com/Malak187"
            }
            developer {
                id = "muhammededrees"
                name = "Muhammed Edrees"
                url = "https://github.com/MuhammedEdrees"
            }
        }
        scm {
            url = "https://github.com/devvura/safe-image/"
            connection = "scm:git:git://github.com/devvura/safe-image.git"
            developerConnection = "scm:git:ssh://git@github.com/devvura/safe-image.git"
        }
    }
}
