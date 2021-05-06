plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-android-extensions")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
    id("androidx.navigation.safeargs.kotlin")
}

android {
    compileSdkVersion(AppConfig.compileSdk)

    defaultConfig {
        minSdkVersion(AppConfig.minSdk)
        targetSdkVersion(AppConfig.targetSdk)
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
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

    buildFeatures {
        dataBinding = true
    }
}

dependencies {
    implementation(project(ModuleDependencies.Libraries.CORE))
	implementation(AppDependencies.androidLibraries)
    implementation(AppDependencies.dependencyInjectionLibraries)
    implementation(AppDependencies.networkLibraries)
    implementation(AppDependencies.navigationLibraries)
    compileOnly(AppDependencies.hiltAssistedInject)
    kapt(AppDependencies.kaptLibraries)
    testImplementation(AppDependencies.testLibraries)
    androidTestImplementation(AppDependencies.androidTestLibraries)
}