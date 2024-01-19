/**
 * Base plugin for Kotlin
 */
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

// This was only required for cloud
//    maven {
//        name = "sonatypeOssSnapshots"
//        url = uri("https://oss.sonatype.org/content/repositories/snapshots/")
//    }

    maven {
        name = "aikar"
        url = uri("https://repo.aikar.co/content/groups/aikar/")
        content {
            includeGroup("co.aikar")
        }
    }
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
