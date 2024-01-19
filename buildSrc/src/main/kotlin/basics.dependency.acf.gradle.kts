import org.gradle.kotlin.dsl.maven

plugins {
    `java-library`
}

repositories {
    maven {
        name = "spigotmc"
        url = uri("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    }
}

dependencies {
    api("co.aikar:acf-paper:0.5.1-SNAPSHOT")
}

tasks.compileJava {
    options.compilerArgs.add("-parameters")
    options.isFork = true
}