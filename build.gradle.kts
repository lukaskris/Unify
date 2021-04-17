// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google()
        jcenter()
        maven { setUrl("https://jitpack.io") }
    }

    dependencies {
        classpathPlugin(AppDependencies.buildPlugins)
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