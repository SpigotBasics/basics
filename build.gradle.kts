import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    base
    id("basics.kotlin-conventions")
    id("com.github.johnrengelman.shadow") apply false
}

tasks.register("copyAllToTestServer") {
    group  = "basics"
    description = "Copies the plugin and all modules to the test server"
    dependsOn("plugin:copyPluginToTestServer")
    dependsOn("modules:copyAllModulesToTestServer")
}

tasks.register<Zip>("zipDistribution") {
    group = "basics"
    description = "Bundle the plugin and all modules into a single zip file."

    archiveFileName = "basics-$version.zip"
    destinationDirectory = file("build/dist")

    from(project(":plugin").tasks.getByName("shadowJar", ShadowJar::class).archiveFile)

    for(module in project(":modules").subprojects) {
        from(module.tasks.getByName("shadowJar", ShadowJar::class).archiveFile) {
            into("Basics/modules")
        }
    }
}