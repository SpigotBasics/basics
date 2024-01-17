import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

/**
 * Used for all modules
 */

plugins {
    id("basics.kotlin-conventions")
    id("basics.dependency.spigot-api")
    `java-library`
    id("com.github.johnrengelman.shadow")
}

apply(plugin = "com.github.johnrengelman.shadow")

dependencies {
    compileOnly(project(":core"))
}

tasks.build {
    dependsOn("shadowJar")
}

tasks.getByName("shadowJar", ShadowJar::class).apply {
    archiveClassifier.set("shaded")
    dependencies {
        exclude(dependency("org.jetbrains.kotlin:kotlin-stdlib"))
        exclude(dependency("org.jetbrains:annotations"))
    }
}