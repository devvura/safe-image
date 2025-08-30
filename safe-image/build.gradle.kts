import com.vanniktech.maven.publish.SonatypeHost

plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.vanniktech.mavenPublish)
}

group = "io.github.devvura"
version = "1.0.0"

android {
    namespace = "io.github.devvura.safeimage.library"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        mlModelBinding = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.tensorflow.lite.support)
    implementation(libs.tensorflow.lite.metadata)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation(libs.bundles.coilXml)
}

mavenPublishing {
    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)

    signAllPublications()

    coordinates(group.toString(), "safe-image", version.toString())

    pom {
        name = "Safe Image"
        description = "An android xml library that helps you to detect and blur NSFW images in your Android applications."
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
