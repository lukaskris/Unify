import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.kotlin.dsl.project

object AppDependencies {
    const val androidGradle = "com.android.tools.build:gradle:${Versions.androidGradleVersion}"
    const val kotlinGradlePlugin =
        "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlinVersion}"
    const val navigationGradlePlugin =
        "androidx.navigation:navigation-safe-args-gradle-plugin:${Versions.safeArgsNavigationVersion}"
    const val hiltGradlePlugin =
        "com.google.dagger:hilt-android-gradle-plugin:${Versions.hiltVersion}"

    //std lib
    private const val kotlinStdLib =
        "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Versions.kotlinVersion}"

    //android ui
    private const val material = "com.google.android.material:material:${Versions.material}"
    private const val appcompat = "androidx.appcompat:appcompat:${Versions.appcompat}"
    private const val coreKtx = "androidx.core:core-ktx:${Versions.coreKtx}"
    private const val lifecycleKtx =
        "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.lifecycleKtx}"
    private const val fragmentKtx = "androidx.fragment:fragment-ktx:${Versions.fragmentKtx}"
    private const val constraintLayout =
        "androidx.constraintlayout:constraintlayout:${Versions.constraintLayout}"
    private const val multiDex = "androidx.multidex:multidex:${Versions.multidexVersion}"
    private const val swipeRefreshVersion =
        "androidx.swiperefreshlayout:swiperefreshlayout:${Versions.swipeRefreshVersion}"

    // shimmer
    private const val shimmer = "com.facebook.shimmer:shimmer:${Versions.shimmer}"

    // glide
    private const val glide = "com.github.bumptech.glide:glide:${Versions.glide}"


    private const val exoPlayer = "com.google.android.exoplayer:exoplayer:${Versions.exoPlayer}"

    val androidLibraries = arrayListOf<String>().apply {
        add(kotlinStdLib)
        add(coreKtx)
        add(fragmentKtx)
        add(lifecycleKtx)
        add(appcompat)
        add(material)
        add(multiDex)
        add(swipeRefreshVersion)
        add(constraintLayout)
        add(shimmer)
        add(exoPlayer)
        add(glide)
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
        add("implementation", dependency)
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