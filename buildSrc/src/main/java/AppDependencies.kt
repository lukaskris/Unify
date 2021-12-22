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
        "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Versions.kotlinVersion}"

    //android ui
    const val material = "com.google.android.material:material:${Versions.material}"
    private const val appcompat = "androidx.appcompat:appcompat:${Versions.appcompat}"
    private const val coreKtx = "androidx.core:core-ktx:${Versions.coreKtx}"
    private const val lifecycleKtx =
        "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.lifecycleKtx}"
    private const val constraintLayout =
        "androidx.constraintlayout:constraintlayout:${Versions.constraintLayout}"
    private const val multiDex = "androidx.multidex:multidex:${Versions.multidexVersion}"
    private const val swipeRefreshVersion =
        "androidx.swiperefreshlayout:swiperefreshlayout:${Versions.swipeRefreshVersion}"

    // shimmer
    private const val shimmer = "com.facebook.shimmer:shimmer:${Versions.shimmer}"

    // glide

    private const val glide = "com.github.bumptech.glide:glide:${Versions.glide}"
    private const val glideCompiler = "com.github.bumptech.glide:compiler:${Versions.glide}"

    const val bom = "com.google.firebase:firebase-bom:${Versions.bomFirebaseVersion}"
    private const val crashlytics = "com.google.firebase:firebase-crashlytics-ktx"
    private const val analytics = "com.google.firebase:firebase-analytics-ktx"
    private const val config = "com.google.firebase:firebase-config-ktx"
    private const val messaging = "com.google.firebase:firebase-messaging-ktx"


    // room
    private const val roomRuntime = "androidx.room:room-runtime:${Versions.room}"
    private const val roomCompiler = "androidx.room:room-compiler:${Versions.room}"
    private const val roomKtx = "androidx.room:room-ktx:${Versions.room}"
    private const val jdbcForSupportM1 = "org.xerial:sqlite-jdbc:${Versions.jdbc}"

    // jetpack
    private const val navigationRuntime =
        "androidx.navigation:navigation-runtime-ktx:${Versions.jetpackNavigation}"
    private const val navigationFragment =
        "androidx.navigation:navigation-fragment-ktx:${Versions.jetpackNavigation}"
    private const val navigationDynamicFeatures =
        "androidx.navigation:navigation-dynamic-features-fragment:${Versions.jetpackNavigation}"
    private const val navigationUiKtx =
        "androidx.navigation:navigation-ui-ktx:${Versions.jetpackNavigation}"


    // cameraX android
    private const val cameraXCore = "androidx.camera:camera-core:${Versions.cameraxVersion}"
    private const val cameraX = "androidx.camera:camera-camera2:${Versions.cameraxVersion}"
    private const val cameraXLifecycle =
        "androidx.camera:camera-lifecycle:${Versions.cameraxVersion}"
    private const val cameraXView = "androidx.camera:camera-view:${Versions.cameraViewVersion}"
    private const val cameraXExtension =
        "androidx.camera:camera-extensions:${Versions.cameraViewVersion}"


    // hilt
    private const val hilt = "com.google.dagger:hilt-android:${Versions.hiltVersion}"
    private const val hiltCompiler =
        "com.google.dagger:hilt-android-compiler:${Versions.hiltVersion}"
    private const val hiltNavigation =
        "androidx.hilt:hilt-navigation-fragment:${Versions.hiltCompilerVersion}"
    private const val hiltTest = "com.google.dagger:hilt-android-testing:${Versions.hiltVersion}"
    private const val hiltViewModelAndroidX = "androidx.hilt:hilt-lifecycle-viewmodel:1.0.0-alpha03"
    private const val hiltCompilerAndroidX =
        "androidx.hilt:hilt-compiler:${Versions.hiltCompilerVersion}"
    private const val hiltAssistedInjectProcessor =
        "com.squareup.inject:assisted-inject-processor-dagger2:${Versions.hiltAssistedVersion}"
    const val hiltAssistedInject =
        "com.squareup.inject:assisted-inject-annotations-dagger2:${Versions.hiltAssistedVersion}"

    // chucker
    const val chucker = "com.github.chuckerteam.chucker:library:${Versions.chucker}"
    const val chuckerNoOp = "com.github.chuckerteam.chucker:library-no-op:${Versions.chucker}"


    private const val retrofit = "com.squareup.retrofit2:retrofit:${Versions.retrofit}"
    const val okHttpInterceptor =
        "com.squareup.okhttp3:logging-interceptor:${Versions.okHttpInterceptor}"

    // moshi
    const val moshi = "com.squareup.moshi:moshi-kotlin:${Versions.moshiVersion}"
    private const val moshiRetrofitFactory =
        "com.squareup.retrofit2:converter-moshi:${Versions.moshiRetrofitFactoryVersion}"
    private const val moshiCodegen =
        "com.squareup.moshi:moshi-kotlin-codegen:${Versions.moshiVersion}"

    const val exoPlayer = "com.google.android.exoplayer:exoplayer:${Versions.exoPlayer}"
    private const val easyPermission = "pub.devrel:easypermissions:3.0.0"
    const val guava = "com.google.guava:guava:30.1-android"

    // paging
    private const val pagingRuntime = "androidx.paging:paging-runtime:${Versions.pagingVersion}"

    // zxing
    private const val barcodeScanner = "me.dm7.barcodescanner:zxing:${Versions.barcodeScannerVersion}"

    // timber
    const val timber = "com.jakewharton.timber:timber:${Versions.timberVersion}"

    private const val toasty = "com.github.GrenderG:Toasty:${Versions.toastyVersion}"

    private const val securityX = "androidx.security:security-crypto:${Versions.securityVersion}"

    private const val commonIO = "commons-io:commons-io:${Versions.commonIoVersion}"


    // test libs
    private const val junit = "junit:junit:${Versions.junit}"
    private const val mockk = "io.mockk:mockk:${Versions.mockk}"
    private const val turbine = "app.cash.turbine:turbine:${Versions.turbine}"
    private const val extJUnit = "androidx.test.ext:junit:${Versions.extJunit}"

    // instrument test libs
    private const val truth = "com.google.truth:truth:${Versions.truthVersion}"
    private const val archCoreTesting =
        "androidx.arch.core:core-testing:${Versions.archCoreVersion}"

    val kaptLibraries = arrayListOf<String>().apply {
        add(hiltCompiler)
        add(hiltCompilerAndroidX)
        add(roomCompiler)
        add(glideCompiler)
        add(moshiCodegen)
        add(hiltAssistedInjectProcessor)
    }

    val dependencyInjectionLibraries = arrayListOf<String>().apply {
        add(hilt)
        add(hiltNavigation)
        add(hiltViewModelAndroidX)
    }

    val networkLibraries = arrayListOf<String>().apply{
        add(retrofit)
        add(moshi)
        add(moshiRetrofitFactory)
        add(okHttpInterceptor)
    }

    val androidLibraries = arrayListOf<String>().apply {
        add(kotlinStdLib)
        add(coreKtx)
        add(lifecycleKtx)
        add(appcompat)
        add(material)
        add(multiDex)
        add(swipeRefreshVersion)
        add(constraintLayout)
        add(shimmer)
        add(exoPlayer)
        add(glide)
        add(bom)
        add(analytics)
        add(config)
        add(toasty)
        add(securityX)
        add(commonIO)
        add(messaging)
        add(crashlytics)
        add(easyPermission)
        add(pagingRuntime)
        add(barcodeScanner)
        add(timber)
    }

    val persistenceLibraries = arrayListOf<String>().apply{
        add(roomRuntime)
        add(jdbcForSupportM1)
        add(roomKtx)
    }

    val navigationLibraries = arrayListOf<String>().apply{
        add(navigationFragment)
        add(navigationUiKtx)
        add(navigationRuntime)
        add(navigationDynamicFeatures)
    }

    val cameraXLibraries = arrayListOf(
        cameraXCore, cameraX, cameraXView, cameraXLifecycle, cameraXExtension
    )
    val testLibraries = arrayListOf<String>().apply {
        add(junit)
        add(mockk)
        add(truth)
        add(archCoreTesting)
    }

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