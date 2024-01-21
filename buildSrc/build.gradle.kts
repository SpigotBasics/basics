plugins {
    `kotlin-dsl`
    kotlin("jvm")
}

repositories {
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    // Dirty hack to make libs from VersionCatalogs work // Thanks @Matt from HelpChat
    implementation(files(libs.javaClass.superclass.protectionDomain.codeSource.location))

    implementation(libs.kotlin.gradle.plugin)
    implementation(libs.shadow)
}