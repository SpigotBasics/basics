import java.io.FileFilter

pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
}

// TODO: Set this up for each and every plugin & dependency & version & buildSrc
dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
        }
    }
}

plugins {
    kotlin("jvm") version "1.9.20" apply false
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.7.0"
}

rootProject.name = "basics"

// Core Module, includes the actual Plugin and interfaces for modules
include("core")
include("plugin")
include("modules")

// Import subprojects from modules folder
val moduleFolders: Array<File> = file("modules").listFiles(FileFilter { it.isDirectory })
    ?: throw RuntimeException("Couldn't find modules folder")

// Add all modules ...
for(moduleFolder in moduleFolders) {

    // ... except for _skeleton
    if(moduleFolder.name.startsWith("_")) continue

    val moduleName = "modules:" + moduleFolder.name
    include(moduleName)
    project(":${moduleName}").projectDir = moduleFolder
}