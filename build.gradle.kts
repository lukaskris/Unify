// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google()
        jcenter()
        maven { setUrl("https://jitpack.io") }
    }

    dependencies {
        classpath("com.android.tools.build:gradle:4.1.3")
        classpath(AppDependencies.androidGradle)
        classpath(AppDependencies.kotlinGradlePlugin)
        classpath(AppDependencies.navigationGradlePlugin)
        classpath(AppDependencies.hiltGradlePlugin)
    }
}

plugins {
    id ("com.diffplug.spotless") version "5.2.0"
}

allprojects {
    repositories {
        google()
        jcenter()
        mavenCentral()
        maven {
            setUrl("https://jitpack.io")
        }
    }
}

spotless {
    kotlin {
        target("**/*.kt")
        ktlint(Versions.kotlinVersion).userData(mapOf("max_line_length" to "100"))
    }
}