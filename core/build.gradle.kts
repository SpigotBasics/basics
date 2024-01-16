import org.jetbrains.dokka.gradle.DokkaTask

plugins {
    id("basics.kotlin-conventions")
    id("basics.dependency.spigot-api")
    id("org.jetbrains.dokka") version "1.9.10"
}

dependencies {
    // why you no workings cloud?
//    implementation("cloud.commandframework:cloud-core:1.7.1")
//    implementation("cloud.commandframework:cloud-bukkit:2.0.0-SNAPSHOT")
}

tasks.withType(DokkaTask::class).configureEach {
    dokkaSourceSets.configureEach {
        //includeNonPublic.set(false)
    }
}