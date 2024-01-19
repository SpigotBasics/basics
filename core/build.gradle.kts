import org.jetbrains.dokka.gradle.DokkaTask

plugins {
    id("basics.kotlin-conventions")
    id("basics.dependency.spigot-api")
    id("org.jetbrains.dokka") version "1.9.10"
}

dependencies {
    api("co.aikar:acf-paper:0.5.1-SNAPSHOT")
    //implementation(kotlin("reflect"))
    api("net.kyori:adventure-api:4.14.0")
    api("net.kyori:adventure-platform-bukkit:4.3.1")
}

tasks.withType(DokkaTask::class).configureEach {
    dokkaSourceSets.configureEach {
        //includeNonPublic.set(false)
    }
}
