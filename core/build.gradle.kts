plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-parcelize")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
    id("maven-publish")
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

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
        kotlinOptions.jvmTarget = JavaVersion.VERSION_11.toString()
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_11.toString()
        moduleName = "id.co.app.core"
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

    buildFeatures {
        dataBinding = true
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    api(project(":components"))
    implementation(AppDependencies.kotlinStdLib)
    implementation(AppDependencies.androidLibraries)
    implementation(AppDependencies.glide)
    implementation(AppDependencies.glideOkHttp){
        exclude(group="glide-parent")
    }
    implementation(AppDependencies.roomRuntime)
    implementation(AppDependencies.pagingRuntime)
    implementation(AppDependencies.shimmer)
    implementation(AppDependencies.moshi)
    implementation(AppDependencies.moshiRetrofitFactory)
    implementation(AppDependencies.toasty)
    implementation(AppDependencies.dependencyInjectionLibraries)
    implementation(AppDependencies.guava) {
        exclude(group = "com.google.guava", module = "listenablefuture")
    }


    compileOnly(AppDependencies.hiltAssistedInject)
    kapt(AppDependencies.kaptLibraries)

}

project.ext {
    set("artifactId", "core")
    set("groupId", "id.co.app")
    set("versionName", "1.0.5")
    set("artifactName", "core")
}

apply {
    from("$rootDir/publish.gradle")
}