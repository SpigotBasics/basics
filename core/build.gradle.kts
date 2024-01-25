import org.jetbrains.dokka.gradle.DokkaTask

plugins {
    id("basics.kotlin-conventions")
    id("basics.dependency.spigot-api")
    id("basics.dependency.acf")
    id("basics.dependency.placeholderapi")
    id("org.jetbrains.dokka") version "1.9.10"
    //libs.plugins.dokka // TODO: Fix this not working because of missing import
}

dependencies {
//    api("net.kyori:adventure-api:4.14.0")
//    api("net.kyori:adventure-platform-bukkit:4.3.1")
    api(libs.adventure.api)
    api(libs.adventure.bukkit)
    api(libs.adventure.minimessage)
    api(libs.adventure.text.serializer.legacy)
    api(libs.paperlib)
}

tasks.withType(DokkaTask::class).configureEach {
    dokkaSourceSets.configureEach {
        //includeNonPublic.set(false)
    }
}

tasks.processResources {
    filesMatching("rusty-spigot-threshold") {
        expand("version" to libs.versions.spigot.get())
    }
}