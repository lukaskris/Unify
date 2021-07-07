import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties


val libraryGroupId by extra { "id.co.app" }
val libraryArtifactId by extra { "components" }
val libraryVersion by extra { "1.0.0" }
val libraryName by extra { "components" }


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
	implementation(AppDependencies.androidLibraries)
}

project.ext{
    set("artifactId", "components")
    set("groupId", "id.co.app")
    set("versionName", "1.0.0")
    set("artifactName", "components")
//    project.ext {
//        artifactId = 'datepicker'
//        groupId = 'com.tokopedia.unify.compositions'
//        versionName = "${project.unifyDatepickerVersion}"
//        artifactName ="date_picker"
//    }
//
//    apply from: "$rootDir/buildconfig/publish.gradle"
}

apply{
    from("$rootDir/publish.gradle")
}

//
//publishing {
//    (publications) {
//        create<MavenPublication>("aar") {
//            artifactId = libraryArtifactId
//            groupId = libraryGroupId
//            version = libraryVersion
//            artifact("$buildDir/outputs/aar/${libraryName}-release.aar")
//        }
//    }
//}
//
//artifactory{
//    setContextUrl(AppConfig.artifactoryUrl)
//    publish {
//        repository {
//            setRepoKey(AppConfig.artifactoryKey)
//            setUsername(AppConfig.artifactoryUsername)
//            setPassword(AppConfig.artifactoryPassword)
//            setMavenCompatible(true)
//        }
//    }
//    resolve {
//        repository {
//            setRepoKey("app-gradle-dev")
//            setUsername(AppConfig.artifactoryUsername)
//            setPassword(AppConfig.artifactoryPassword)
//            setMavenCompatible(true)
//        }
//    }
//}