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
        renderscriptTargetApi = AppConfig.minSdk
        renderscriptSupportModeEnabled = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }

    lintOptions {
        isAbortOnError = false
    }

    packagingOptions {
        exclude("META-INF/DEPENDENCIES")
        exclude("META-INF/LICENSE")
        exclude("META-INF/LICENSE.txt")
        exclude("META-INF/license.txt")
        exclude("META-INF/NOTICE")
        exclude("META-INF/NOTICE.txt")
        exclude("META-INF/notice.txt")
        exclude("META-INF/ASL2.0")
        exclude("META-INF/*.kotlin_module")
    }

    buildFeatures {
        dataBinding = true
    }
}

dependencies {
    implementation(project(":querybuilder"))
    implementation(project(":components"))
    implementation(AppDependencies.androidLibraries)
    implementation(AppDependencies.dependencyInjectionLibraries)
    implementation(AppDependencies.networkLibraries)
    implementation(AppDependencies.persistenceLibraries)
    implementation(AppDependencies.navigationLibraries)
    implementation(AppDependencies.guava) {
        exclude(group = "com.google.guava", module = "listenablefuture")
    }

    debugImplementation(AppDependencies.chucker)
    releaseImplementation(AppDependencies.chuckerNoOp)

    compileOnly(AppDependencies.hiltAssistedInject)
    kapt(AppDependencies.kaptLibraries)

}

project.ext{
    set("artifactId", "core")
    set("groupId", "id.co.app")
    set("versionName", "1.0.0")
    set("artifactName", "core")
}

apply{
    from("$rootDir/publish.gradle")
}