import org.gradle.kotlin.dsl.`kotlin-dsl`

repositories {
    google()
    jcenter()
}

plugins {
    `kotlin-dsl`
}

kotlinDslPluginOptions {
    experimentalWarning.set(false)
}