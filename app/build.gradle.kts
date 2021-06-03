/*
 * Created by Hendry Syamsudin on 22/12/20 17:36
 * Copyright (c) APP Sinarmas 2020. All rights reserved.
 * Last modified 22/12/20 17:34
 */

plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-android-extensions")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
    id("androidx.navigation.safeargs.kotlin")
}

android {
    compileSdkVersion(30)
    buildToolsVersion("30.0.3")
    buildFeatures {
        dataBinding = true
    }
    defaultConfig {
        applicationId = "id.co.app.source"
        minSdkVersion(AppConfig.minSdk)
        targetSdkVersion(AppConfig.targetSdk)
        versionCode = AppConfig.versionCode
        versionName = AppConfig.versionName
        vectorDrawables.useSupportLibrary = true
        multiDexEnabled = true
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        javaCompileOptions {
            annotationProcessorOptions {
                arguments["dagger.hilt.disableModulesHaveInstallInCheck"] = "true"
            }
        }
    }

    buildTypes {
        getByName("debug") {
            debuggable(true)
            isZipAlignEnabled = true
            isMinifyEnabled = false
        }
        getByName("release") {
            isDebuggable = false
            isZipAlignEnabled = true
            isMinifyEnabled = true
            isShrinkResources = true
            debuggable(false)
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
        kotlinOptions {
            jvmTarget = "1.8"
            freeCompilerArgs += "-Xallow-jvm-ir-dependencies"
            freeCompilerArgs += "-Xopt-in=kotlinx.coroutines.ExperimentalCoroutinesApi"
            freeCompilerArgs += "-Xopt-in=kotlinx.coroutines.FlowPreview"
        }
    }
}

subprojects {
    project.configurations.all {
        resolutionStrategy.eachDependency {
            if (requested.group == "androidx.core" &&
                !requested.name.contains("androidx")
            ) {
                useVersion("${Versions.appcompat}")
            }
        }
        resolutionStrategy.force("org.antlr:antlr4-runtime:4.7.1")
        resolutionStrategy.force("org.antlr:antlr4-tool:4.7.1")
    }
}

dependencies {
    implementationProject(ModuleDependencies.libraries)
    implementationProject(ModuleDependencies.features)
    implementation(AppDependencies.androidLibraries)
    implementation(AppDependencies.dependencyInjectionLibraries)
    implementation(AppDependencies.networkLibraries)
    implementation(AppDependencies.persistenceLibraries)
    implementation(AppDependencies.navigationLibraries)

    compileOnly(AppDependencies.hiltAssistedInject)
    kapt(AppDependencies.kaptLibraries)

    androidTestImplementation(AppDependencies.androidTestLibraries)
}