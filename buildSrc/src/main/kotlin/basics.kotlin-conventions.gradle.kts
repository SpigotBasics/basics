import gradle.kotlin.dsl.accessors._e054d9723d982fdb55b1e388b8ab0cbf.compileJava

/**
 * Base plugin for Kotlin
 */

group = getGroupId()
version = "1.0-SNAPSHOT"

plugins {
    kotlin("jvm")
}

repositories {
    mavenCentral()

    maven {
        name = "spigotmc"
        url = uri("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
        content {
            includeGroup("org.spigotmc")
        }
    }

    maven {
        name = "aikar"
        url = uri("https://repo.aikar.co/content/groups/aikar/")
        content {
            includeGroup("co.aikar")
        }
    }
}

kotlin {
    jvmToolchain(8)
}

tasks.compileKotlin {
    kotlinOptions {
        javaParameters = true
    }
}