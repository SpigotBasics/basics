import java.io.FileFilter

pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
}

plugins {
    kotlin("jvm") version "1.9.20" apply false
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.7.0"
    id("com.gradle.enterprise") version("3.16.1")
}

rootProject.name = "basics"

// Data classes used in multiple modules
include("common")

// Core Module, includes the actual Plugin and interfaces for modules
include("core")

// Facades for Bukkit and Paper
include("pipe:facade")
include("pipe:spigot")
include("pipe:paper")

// Actual JavaPlugin
include("plugin")

// Parent for all modules
// include("modules")

// Bootstrap plugin, so people don't have to extract a zip file (70% of people would just put it into the plugins folder)
include("bootstrap")

// Import subprojects from modules folder
val moduleFolders: Array<File> =
    file("modules").listFiles(FileFilter { it.isDirectory })
        ?: throw RuntimeException("Couldn't find modules folder")

// Add all modules ...
for (moduleFolder in moduleFolders) {

    // ... except for _skeleton
    if (moduleFolder.name.startsWith("_")) continue

    val moduleName = "modules:" + moduleFolder.name
    include(moduleName)
    project(":$moduleName").projectDir = moduleFolder
}

gradleEnterprise {
    if (System.getenv("CI") != null) {
        buildScan {
            termsOfServiceUrl = "https://gradle.com/terms-of-service"
            termsOfServiceAgree = "yes"
        }
    }
}
