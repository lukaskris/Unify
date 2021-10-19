plugins {
    id("com.android.library")
    id("kotlin-android")
    id("maven-publish")
    id("com.jfrog.artifactory")
}

android {
    compileSdk = AppConfig.compileSdk
    buildToolsVersion = AppConfig.buildToolsVersion
    defaultConfig {
        minSdk = AppConfig.minSdk
        targetSdk = AppConfig.targetSdk
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }

}

dependencies {
    compileOnly(AppDependencies.okHttpInterceptor)
    compileOnly(AppDependencies.timber)
}


project.ext{
    set("artifactId", "retrofiturlhandler")
    set("groupId", "id.co.app")
    set("versionName", "1.0.0")
    set("artifactName", "retrofiturlhandler")
}

apply{
    from("$rootDir/publish.gradle")
}