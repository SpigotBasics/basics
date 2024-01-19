import org.jetbrains.dokka.gradle.DokkaTask

plugins {
    id("basics.kotlin-conventions")
    id("basics.dependency.spigot-api")
    id("basics.dependency.acf")
    id("org.jetbrains.dokka") version "1.9.10"
}

dependencies {
    api("net.kyori:adventure-api:4.14.0")
    api("net.kyori:adventure-platform-bukkit:4.3.1")
}

tasks.withType(DokkaTask::class).configureEach {
    dokkaSourceSets.configureEach {
        //includeNonPublic.set(false)
    }
}
