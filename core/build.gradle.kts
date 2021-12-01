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
        resources.excludes.add("META-INF/DEPENDENCIES")
        resources.excludes.add("META-INF/LICENSE")
        resources.excludes.add("META-INF/LICENSE.txt")
        resources.excludes.add("META-INF/license.txt")
        resources.excludes.add("META-INF/NOTICE")
        resources.excludes.add("META-INF/NOTICE.txt")
        resources.excludes.add("META-INF/notice.txt")
        resources.excludes.add("META-INF/ASL2.0")
    }

    buildFeatures {
        dataBinding = true
    }
}

dependencies {
    api(project(":components"))
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