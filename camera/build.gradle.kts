plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-parcelize")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
    id("maven-publish")
    id("com.jfrog.artifactory")
}

android {
    compileSdk = AppConfig.compileSdk
	buildToolsVersion = AppConfig.buildToolsVersion

    defaultConfig {
        minSdk = AppConfig.minSdk
        targetSdk = AppConfig.targetSdk
        vectorDrawables.useSupportLibrary = true
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
        moduleName = "id.co.app.camera"
    }

    buildFeatures {
        dataBinding = true
    }

    packagingOptions {
        resources.excludes.add("META-INF/DEPENDENCIES")
        resources.excludes.add("META-INF/LICENSE")
        resources.excludes.add("META-INF/LICENSE.txt")
        resources.excludes.add("META-INF/license.txt")
        resources.excludes.add("META-INF/NOTICE")
        resources.excludes.add("META-INF/ASL2.0")
        resources.excludes.add("META-INF/notice.txt")
        resources.excludes.add("META-INF/NOTICE.txt")
//        exclude("META-INF/*.kotlin_module")
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation(project(":core"))
    implementation(AppDependencies.androidLibraries)
    implementation(AppDependencies.dependencyInjectionLibraries)
    implementation(AppDependencies.cameraXLibraries)
    implementation(AppDependencies.navigationLibraries)
    kapt(AppDependencies.kaptLibraries)
    compileOnly(AppDependencies.hiltAssistedInject)
}

project.ext{
    set("artifactId", "camera")
    set("groupId", "id.co.app")
    set("versionName", "1.1.3")
    set("artifactName", "camera")
}

apply{
    from("$rootDir/publish.gradle")
}