
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
    compileOnly(project(":nms:facade"))
    implementation(project(":nms:aggregator", "shadow"))
    // implementation(project(":common"))
    // implementation(kotlin("reflect"))
}

// tasks {
//     build {
//         dependsOn(shadowJar)
//     }
// }

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

    minimize {
        exclude(project(":core"))
        exclude(project(":pipe:facade"))
        exclude(project(":pipe:spigot"))
        exclude(project(":pipe:paper"))
    }
}

tasks.register("copyPluginToTestServer", Copy::class) {
    group = "basics custom testserver"
    description = "Copies the plugin to the test server"
    from(tasks.shadowJar.get().archiveFile)
    into(getServerPluginsDirectory())
}
