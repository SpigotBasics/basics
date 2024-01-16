import gradle.kotlin.dsl.accessors._f80d018c6682eda65edc89328dbbdb35.javaToolchains

/**
 * Base plugin for Kotlin
 */
plugins {
    kotlin("jvm")
}

repositories {
    mavenCentral()
}

/**
 * Returns a proper group ID for this subproject, e.g. "com.github.spigotbasics.modules" for modules/tpa
 * TODO: This should be moved into the main build script into allprojects
 */
fun getGroupId(): String {
    val groupId = "com.github.spigotbasics"
    val myParentDir = projectDir.parentFile
    if (rootDir == myParentDir) {
        return groupId
    }
    val relativeGroupId = myParentDir.relativeTo(rootDir).path.replace(File.separatorChar, '.')
    if (relativeGroupId.isEmpty()) {
        throw RuntimeException("Couldn't determine relative group ID for $projectDir")
    }
    return "${groupId}.${relativeGroupId}"
}

group = getGroupId()
version = "1.0-SNAPSHOT"

kotlin {
    jvmToolchain(8)
}