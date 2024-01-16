import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("basics.kotlin-conventions")
    id("com.github.johnrengelman.shadow") version "8.1.1" apply false
    id("org.jetbrains.dokka") version "1.9.10" apply false
}



val testServerDir = File(System.getProperty("TEST_SERVER_PATH","/Users/mfnalex/mctest/"))
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
            from(it.tasks.getByName("jar", Jar::class).archiveFile)
        }
        into(basicsModulesDir)
    }

    register("copyAllToTestServer") {
        group = "test"

        if(!testServerDir.isDirectory) {
            throw RuntimeException("Test server directory does not exist! Set environment var TEST_SERVER_PATH to the path of your test server.")
        }
        basicsModulesDir.mkdirs()

        dependsOn(copyPluginToTestServer)
        dependsOn(copyModulesToTestServer)
    }
}