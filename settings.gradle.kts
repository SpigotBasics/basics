import java.io.FileFilter

pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
}


plugins {
    kotlin("jvm") version "1.9.22" apply false
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.7.0"
}

rootProject.name = "basics"

// Core Module, includes the actual Plugin and interfaces for modules
include("core")
include("plugin")

// Import subprojects from modules folder
val moduleFolders: Array<File> = file("modules").listFiles(FileFilter { it.isDirectory })
    ?: throw RuntimeException("Couldn't find modules folder")

for(moduleFolder in moduleFolders) {
    val moduleName: String = moduleFolder.name
    include(moduleName)
    project(":${moduleName}").projectDir = moduleFolder
}