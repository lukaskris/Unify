plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-android-extensions")
    id("kotlin-kapt")
    id("maven-publish")
    id("maven")
    id("com.jfrog.artifactory")
}

android {
    compileSdkVersion(AppConfig.compileSdk)
	buildToolsVersion(AppConfig.buildToolsVersion)

    defaultConfig {
        minSdkVersion(AppConfig.minSdk)
        targetSdkVersion(AppConfig.targetSdk)

        vectorDrawables.useSupportLibrary = true
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    implementation (AppDependencies.kotlinStdLib)
    implementation (AppDependencies.material)
    implementation (AppDependencies.exoPlayer)
    implementation (AppDependencies.glide)
}

project.ext{
    set("artifactId", "components")
    set("groupId", "id.co.app")
    set("versionName", "1.0.0")
    set("artifactName", "components")
}

apply{
    from("$rootDir/publish.gradle")
}