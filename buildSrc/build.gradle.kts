import org.gradle.kotlin.dsl.`kotlin-dsl`

repositories {
    google()
    jcenter()
    mavenCentral()
    maven { setUrl("https://jitpack.io") }
}

plugins {
    `kotlin-dsl`
}
