// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google()
        jcenter()
        mavenCentral()
        maven { setUrl("https://jitpack.io") }
    }

    dependencies {
        classpath("com.android.tools.build:gradle:4.1.3")
        classpath(AppDependencies.kotlinGradlePlugin)
        classpath ("org.jfrog.buildinfo:build-info-extractor-gradle:4.24.10")
    }
}

plugins {
    id ("com.diffplug.spotless") version "5.2.0"
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

spotless {
    kotlin {
        target("**/*.kt")
        ktlint(Versions.kotlinVersion).userData(mapOf("max_line_length" to "100"))
    }
}
