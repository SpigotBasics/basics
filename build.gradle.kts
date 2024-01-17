import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("basics.kotlin-conventions")
    id("com.github.johnrengelman.shadow") apply false
}

// TODO: Move this into a buildSrc script
val testServerDir = File(System.getProperty("TEST_SERVER_PATH", "/Users/mfnalex/mctest"))
val pluginsDir = File(testServerDir, "plugins")
val basicsPluginDir = File(pluginsDir, "Basics")
val basicsModulesDir = File(basicsPluginDir, "modules")

tasks {
    val copyPluginToTestServer = register<Copy>("copyPluginToTestServer") {
        from(project(":plugin").tasks.getByName("shadowJar", ShadowJar::class).archiveFile)
        into(pluginsDir)
    }

    val copyModulesToTestServer = register<Copy>("copyModulesToTestServer") {
        project(":modules").subprojects.forEach {
            from(it.tasks.getByName("shadowJar", ShadowJar::class).archiveFile)
        }
        into(basicsModulesDir)
    }

    register("copyAllToTestServer") {
        group = "test"

        if(!testServerDir.isDirectory) {
            error("Test server directory does not exist! Set environment var TEST_SERVER_PATH to the path of your test server.")
        }
        basicsModulesDir.mkdirs()

        dependsOn(copyPluginToTestServer)
        dependsOn(copyModulesToTestServer)
    }
}