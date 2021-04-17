// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
//    ext {
//        // Sdk and tools
//        compileSdkVersion = 30
//        minSdkVersion = 21
//        targetSdkVersion = 30
//
//        // App dependencies
//        appCompatVersion = '1.2.0'
//        archCompomentVersion = '2.1.0'
//        assistedInjectVersion = '0.8.1'
//        constraintLayoutVersion = '2.0.4'
//        coreTestingVersion = '2.1.0'
//        coroutinesVersion = '1.4.3-native-mt'
//        espressoVersion = '3.3.0'
//        fragmentVersion = '1.3.2'
//        glideVersion = '4.12.0'
//        gradleVersion = '4.1.3'
//        gsonVersion = '2.8.6'
//        hiltDaggerVersion = '2.33-beta'
//        hiltCompilerVersion = '1.0.0-beta01'
//        hiltViewModelVersion = '1.0.0-alpha03'
//        junitVersion = '4.13.2'
//        kotlinVersion = '1.4.32'
//        ktlintVersion = '0.38.1'
//        ktxVersion = '1.3.2'
//        lifecycleVersion = '2.2.0'
//        lifecycleLiveDataVersion = '2.3.1'
//        materialVersion = '1.3.0'
//        mockkVersion = '1.10.6'
//        moshiVersion = '1.12.0'
//        moshiConverterVersion = '2.9.0'
//        navigationVersion = '2.3.5'
//        okhttpVersion = '5.0.0-alpha.2'
//        pagingVersion = '3.0.0-beta03'
//        recyclerViewVersion = '1.2.0'
//        retrofitVersion = '2.9.0'
//        roomVersion = '2.3.0-rc01'
//        runnerVersion = '1.0.1'
//        shimmerVersion = '0.5.0'
//        swipeRefreshVersion = '1.1.0'
//        truthVersion = '1.1'
//        testExtJunit = '1.1.2'
//        uiAutomatorVersion = '2.2.0'
//        viewPagerVersion = '1.0.0'
//        workVersion = '2.5.0'
//        moshiVersion = '1.12.0'
//    }

    repositories {
        google()
        jcenter()
        maven { setUrl("https://jitpack.io") }
    }

    dependencies {
        classpathPlugin(AppDependencies.buildPlugins)
    }
}

plugins {
    id ("com.diffplug.spotless") version "5.2.0"
}

allprojects {
    repositories {
        google()
        jcenter()
        mavenCentral()
        maven {
            setUrl("https://jitpack.io")
        }
    }
}

spotless {
    kotlin {
        target("**/*.kt")
        ktlint(Versions.kotlinVersion).userData(mapOf("max_line_length" to "100"))
    }
}