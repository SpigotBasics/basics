/**
 * Base plugin for Kotlin
 */

group = getGroupId()
version = "1.0-SNAPSHOT"

plugins {
    kotlin("jvm")
}

dependencies {
    testImplementation(kotlin("test"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.9.2")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
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
        name = "jeff-media-public"
        url = uri("https://repo.jeff-media.com/public/")
        content {
            includeGroup("com.jeff-media")
            includeGroup("com.github.spigotbasics")
        }
    }

    maven {
        name = "aikar"
        url = uri("https://repo.aikar.co/content/groups/aikar/")
        content {
            includeGroup("co.aikar")
        }
    }

    maven {
        name = "extendedclip"
        url = uri("https://repo.extendedclip.com/content/repositories/placeholderapi/")
        content {
            includeGroup("me.clip")
        }
    }

    maven {
        name = "papermc"
        url = uri("https://papermc.io/repo/repository/maven-public/")
        content {
            includeGroup("io.papermc")
            includeGroup("io.papermc.paper")
        }
    }

    maven {
        name = "devmart-other"
        url = uri("https://nexuslite.gcnt.net/repos/other/")
        content {
            includeGroup("com.tcoded")
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

tasks.test {
    useJUnitPlatform()
}