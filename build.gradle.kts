// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google()
        mavenCentral()
        maven { setUrl("https://jitpack.io") }
    }

    dependencies {
        classpath(AppDependencies.androidGradle)
        classpath(AppDependencies.kotlinGradlePlugin)
        classpath(AppDependencies.jfrogExtractor)
        classpath(AppDependencies.googleGmsService)
        classpath(AppDependencies.crashlyticsGradlePlugin)
        classpath(AppDependencies.hiltGradlePlugin)
    }
}

val artifactory_url: String by project
val artifactory_username: String by project
val artifactory_password: String by project

allprojects {
    apply(plugin = "com.jfrog.artifactory")
    apply(plugin = "maven-publish")

    repositories {
        google()
        jcenter()
        mavenCentral()
        maven {
            setUrl("https://jitpack.io")
        }

        maven {
            url = uri("$artifactory_url/app-gradle-dev-local/")
            credentials {
                username = artifactory_username
                password = artifactory_password
            }
        }
    }
}