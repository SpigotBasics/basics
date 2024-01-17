plugins {
    id("basics.kotlin-conventions")
    id("basics.dependency.spigot-api")
    `java-library`
    id("com.github.johnrengelman.shadow")
}

dependencies {
    implementation(project(":core"))
    implementation(kotlin("reflect"))
}

tasks {
    build {
        dependsOn(shadowJar)
    }
}

tasks.processResources {
    filesMatching("plugin.yml") {
        expand("pluginVersion" to project.version)
    }
}

tasks.jar {
    archiveBaseName = "basics"
}

tasks.shadowJar {
    archiveBaseName = "basics"
    archiveClassifier = "shaded"
    minimize()
}