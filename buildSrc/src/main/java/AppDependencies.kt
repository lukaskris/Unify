import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.kotlin.dsl.project

object AppDependencies {
    const val androidGradle = "com.android.tools.build:gradle:${Versions.androidGradleVersion}"
    const val kotlinGradlePlugin =
        "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlinVersion}"
    const val jfrogExtractor = "org.jfrog.buildinfo:build-info-extractor-gradle:${Versions.jfrogVersion}"
    const val googleGmsService =
        "com.google.gms:google-services:${Versions.googleServiceGms}"
    const val crashlyticsGradlePlugin =
        "com.google.firebase:firebase-crashlytics-gradle:${Versions.crashlyticsPlugin}"
    const val hiltGradlePlugin =
        "com.google.dagger:hilt-android-gradle-plugin:${Versions.hiltVersion}"
    //std lib
    const val kotlinStdLib =
        "org.jetbrains.kotlin:kotlin-stdlib-jdk8:${Versions.kotlinVersion}"

    //android ui
    const val material = "com.google.android.material:material:${Versions.material}"
    const val appcompat = "androidx.appcompat:appcompat:${Versions.appcompat}"
    const val coreKtx = "androidx.core:core-ktx:${Versions.coreKtx}"
    private const val lifecycleKtx =
        "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.lifecycleKtx}"
    private const val activityKtx = "androidx.activity:activity-ktx:${Versions.activityKtx}"
    private const val constraintLayout =
        "androidx.constraintlayout:constraintlayout:${Versions.constraintLayout}"
    private const val multiDex = "androidx.multidex:multidex:${Versions.multidexVersion}"
    private const val swipeRefreshVersion =
        "androidx.swiperefreshlayout:swiperefreshlayout:${Versions.swipeRefreshVersion}"
    const val shimmer = "com.facebook.shimmer:shimmer:${Versions.shimmer}"
    const val glide = "com.github.bumptech.glide:glide:${Versions.glide}"
    const val glideOkHttp = "com.github.bumptech.glide:okhttp3-integration:${Versions.glide}"
    private const val glideCompiler = "com.github.bumptech.glide:compiler:${Versions.glide}"

    const val bom = "com.google.firebase:firebase-bom:${Versions.bomFirebaseVersion}"
    private const val crashlytics = "com.google.firebase:firebase-crashlytics-ktx"
    private const val analytics = "com.google.firebase:firebase-analytics-ktx"
    private const val config = "com.google.firebase:firebase-config-ktx"
    private const val messaging = "com.google.firebase:firebase-messaging-ktx"

    private const val compressor = "id.zelory:compressor:${Versions.compressionVersion}"

    const val roomRuntime = "androidx.room:room-runtime:${Versions.room}"
    const val roomCompiler = "androidx.room:room-compiler:${Versions.room}"
    const val roomKtx = "androidx.room:room-ktx:${Versions.room}"

    const val worker = "androidx.work:work-runtime-ktx:${Versions.worker}"

    // cameraX android
    private const val cameraXCore = "androidx.camera:camera-core:${Versions.cameraxVersion}"
    private const val mlKit = "com.google.android.gms:play-services-mlkit-barcode-scanning:${Versions.mlkitVersion}"
    private const val cameraX = "androidx.camera:camera-camera2:${Versions.cameraxVersion}"
    private const val cameraXLifecycle = "androidx.camera:camera-lifecycle:${Versions.cameraxVersion}"
    private const val cameraXView = "androidx.camera:camera-view:${Versions.cameraViewVersion}"
    private const val cameraXExtension = "androidx.camera:camera-extensions:${Versions.cameraViewVersion}"

    private const val navigationRuntime = "androidx.navigation:navigation-runtime-ktx:${Versions.jetpackNavigation}"
//    const val navigationUiKtx = "androidx.navigation:navigation-ui-ktx:${Versions.jetpackNavigation}"
    // hilt
    const val hilt = "com.google.dagger:hilt-android:${Versions.hiltVersion}"
    const val hiltCompiler =
        "com.google.dagger:hilt-android-compiler:${Versions.hiltVersion}"
    private const val hiltNavigation = "androidx.hilt:hilt-navigation-fragment:${Versions.hiltCompilerVersion}"
    private const val hiltTest = "com.google.dagger:hilt-android-testing:${Versions.hiltVersion}"
    private const val hiltViewModelAndroidX = "androidx.hilt:hilt-lifecycle-viewmodel:1.0.0-alpha03"
    const val hiltCompilerAndroidX =
        "androidx.hilt:hilt-compiler:${Versions.hiltCompilerVersion}"
    const val hiltAssistedInjectProcessor =
        "com.squareup.inject:assisted-inject-processor-dagger2:${Versions.hiltAssistedVersion}"
    const val hiltAssistedInject =
        "com.squareup.inject:assisted-inject-annotations-dagger2:${Versions.hiltAssistedVersion}"

    // chucker
//    const val chucker = "com.github.chuckerteam.chucker:library:${Versions.chucker}"
//    const val chuckerNoOp = "com.github.chuckerteam.chucker:library-no-op:${Versions.chucker}"


    const val moshi = "com.squareup.moshi:moshi-kotlin:${Versions.moshiVersion}"
    const val moshiRetrofitFactory =
        "com.squareup.retrofit2:converter-moshi:${Versions.moshiRetrofitFactoryVersion}"
    private const val moshiCodegen =
        "com.squareup.moshi:moshi-kotlin-codegen:${Versions.moshiVersion}"
    private const val retrofit = "com.squareup.retrofit2:retrofit:${Versions.retrofit}"
    const val okHttpInterceptor =
        "com.squareup.okhttp3:logging-interceptor:${Versions.okHttpInterceptor}"
    const val easyPermission = "pub.devrel:easypermissions:3.0.0"
    const val guava = "com.google.guava:guava:30.1-android"
    const val pagingRuntime = "androidx.paging:paging-runtime:${Versions.pagingVersion}"

    // timber
    const val timber = "com.jakewharton.timber:timber:${Versions.timberVersion}"
    const val toasty = "com.github.GrenderG:Toasty:${Versions.toastyVersion}"
    private const val securityX = "androidx.security:security-crypto:${Versions.securityVersion}"

    private const val commonIO = "commons-io:commons-io:${Versions.commonIoVersion}"

    val kaptLibraries = arrayListOf<String>().apply {
        add(hiltCompiler)
        add(hiltCompilerAndroidX)
        add(glideCompiler)
        add(hiltAssistedInjectProcessor)
    }

    val dependencyInjectionLibraries = arrayListOf<String>().apply {
        add(hilt)
        add(hiltNavigation)
        add(hiltViewModelAndroidX)
    }

    val androidLibraries = arrayListOf<String>().apply {
        add(kotlinStdLib)
        add(coreKtx)
        add(lifecycleKtx)
        add(activityKtx)
        add(material)
        add(multiDex)
        add(constraintLayout)
        add(navigationRuntime)
        add(bom)
        add(analytics)
        add(config)
        add(securityX)
        add(commonIO)
        add(messaging)
        add(crashlytics)
        add(timber)
        add(compressor)
    }
    val cameraXLibraries = arrayListOf(
        cameraXCore, cameraX, cameraXView, cameraXLifecycle, cameraXExtension, mlKit
    )

}

//util functions for adding the different type dependencies from build.gradle.kts file
fun DependencyHandler.classpathPlugin(list: List<String>){
    list.forEach { dependency ->
        add("classpath", dependency)
    }
}

fun DependencyHandler.kapt(list: List<String>) {
    list.forEach { dependency ->
        add("kapt", dependency)
    }
}

fun DependencyHandler.ksp(list: List<String>) {
    list.forEach { dependency ->
        add("ksp", dependency)
    }
}

fun DependencyHandler.implementation(list: List<String>) {
    list.forEach { dependency ->
        if(dependency == AppDependencies.bom) {
            add("implementation", platform(dependency))
        } else {
            add("implementation", dependency)
        }
    }
}

fun DependencyHandler.implementationProject(list: List<String>) {
    list.forEach { dependency ->
        add("implementation", project(dependency))
    }
}

fun DependencyHandler.androidTestImplementation(list: List<String>) {
    list.forEach { dependency ->
        add("androidTestImplementation", dependency)
    }
}

fun DependencyHandler.testImplementation(list: List<String>) {
    list.forEach { dependency ->
        add("testImplementation", dependency)
    }
}