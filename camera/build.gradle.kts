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
    publishing {
        multipleVariants {
            allVariants()
            withJavadocJar()
        }
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
    implementation(AppDependencies.glide)
    implementation(AppDependencies.toasty)
    implementation(AppDependencies.easyPermission)
    kapt(AppDependencies.kaptLibraries)
    compileOnly(AppDependencies.hiltAssistedInject)
}

project.ext{
    set("artifactId", "camera")
    set("groupId", "id.co.app")
    set("versionName", "1.1.9")
    set("artifactName", "camera")
}

afterEvaluate {
    publishing {
        publications {
            register("release", MavenPublication::class){
                // Applies the component for the release build variant.
                // NOTE : Delete this line code if you publish Native Java / Kotlin Library
                from(components["release"])
                 groupId = "id.co.app"
                 artifactId = "camera"
                 version = "1.1.9"
                 artifact("$buildDir/outputs/aar/${project.name}-release.aar") // this is the solution I came up with
             }
         }
    }
}


//apply{
//    from("$rootDir/publish.gradle")
//}