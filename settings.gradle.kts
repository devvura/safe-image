pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "SafeImage"
include(":safe-image-compose")
include(":compose-sample")
include(":safe-image")
include(":xml-sample")
