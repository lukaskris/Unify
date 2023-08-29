plugins {
    id("com.android.library")
    id("kotlin-android")
    id("maven-publish")
    id("kotlin-kapt")
    id("com.jfrog.artifactory")
    id("com.google.devtools.ksp").version("1.6.0-1.0.1")
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
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
        kotlinOptions.jvmTarget = JavaVersion.VERSION_11.toString()
    }

    kotlinOptions {
        jvmTarget = "11"
        moduleName = "id.co.app.retrofiturlhandler"
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
    implementation(AppDependencies.kotlinStdLib)
    implementation(AppDependencies.coreKtx)
    api(AppDependencies.roomRuntime)
    api(AppDependencies.roomKtx)
    api(AppDependencies.worker)
    kapt(listOf(AppDependencies.roomCompiler, AppDependencies.hiltCompiler, AppDependencies.hiltCompilerAndroidX, AppDependencies.hiltAssistedInjectProcessor))
    compileOnly(AppDependencies.okHttpInterceptor)
    compileOnly(AppDependencies.timber)
    implementation(AppDependencies.dependencyInjectionLibraries)
    compileOnly(AppDependencies.hiltAssistedInject)
}


project.ext{
    set("artifactId", "lsdownloader")
    set("groupId", "id.co.app")
    set("versionName", "1.0.0")
    set("artifactName", "lsdownloader")
}

afterEvaluate {
    publishing {
        publications {
            register("release", MavenPublication::class){
                // Applies the component for the release build variant.
                // NOTE : Delete this line code if you publish Native Java / Kotlin Library
                from(components["release"])
                groupId = "id.co.app"
                artifactId = "lsdownloader"
                version = "1.0.0"
                artifact("$buildDir/outputs/aar/${project.name}-release.aar") // this is the solution I came up with
            }
        }
    }
}

apply{
    from("$rootDir/publish.gradle")
}