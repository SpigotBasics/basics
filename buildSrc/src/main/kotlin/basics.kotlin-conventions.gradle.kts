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
