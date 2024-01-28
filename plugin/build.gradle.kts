
plugins {
    id("basics.kotlin-conventions")
    id("basics.dependency.spigot-api")
    `java-library`
    id("com.github.johnrengelman.shadow")
}

dependencies {
    implementation(project(":core", "shadow"))
    implementation(project(":pipe:facade"))
    implementation(project(":pipe:spigot"))
    implementation(project(":pipe:paper"))
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
    // Needs proper setup to not exclude unused API minimize() {}

    //relocate("kotlin", "$SHADED.kotlin")
}

tasks.register("copyPluginToTestServer", Copy::class) {
    group = "testserver"
    description = "Copies the plugin to the test server"
    from(tasks.shadowJar.get().archiveFile)
    into(getServerPluginsDirectory())
}