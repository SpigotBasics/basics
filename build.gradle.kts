import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.kotlin.incremental.deleteRecursivelyOrThrow

version = "git-${getGitCommitHash()}"

plugins {
    base
    // id("basics.kotlin-conventions")
    id("com.github.johnrengelman.shadow") apply false
    id("org.jlleitschuh.gradle.ktlint") version "12.1.0"
    id("xyz.jpenilla.run-paper") version "2.1.0"
}

allprojects {
    repositories {
        mavenCentral()
    }
    afterEvaluate {
        tasks.withType<ShadowJar>().configureEach {
            archiveVersion = ""
            archiveClassifier = "shaded"
        }
    }
}

subprojects {
    version = rootProject.version
}

tasks.register("copyAllToTestServer") {
    group = "basics custom testserver"
    description = "Copies the plugin and all modules to the test server"
    dependsOn("plugin:copyPluginToTestServer")
    dependsOn("copyAllModulesToTestServer")
}

val distribution =
    tasks.register<Copy>("distribution") {
        group = "basics"
        description = "Bundle the plugin and all modules into a single directory."

        dependsOn(tasks.build)

        into("build/dist/basics-$version")

        from(project(":plugin").tasks.getByName("shadowJar", ShadowJar::class).archiveFile)

        project(":modules").subprojects.forEach { module ->
            module.tasks.withType<ShadowJar>().forEach { shadowTask ->
                from(shadowTask.archiveFile) {
                    into("Basics/modules")
                }
            }
        }
    }

tasks.register<Zip>("zipDistribution") {
    group = "basics"
    description = "Bundle the plugin and all modules into a single zip file."

    dependsOn(tasks.build)

    archiveFileName = "basics-$version.zip"
    destinationDirectory = file("build/dist")

    from(project(":plugin").tasks.getByName("shadowJar", ShadowJar::class).archiveFile)
    for (module in project(":modules").subprojects) {
        module.tasks.withType<ShadowJar>().forEach { shadowTask ->
            from(shadowTask.archiveFile) {
                into("Basics/modules")
            }
        }
    }
}

tasks.register("copyAllModulesToTestServer") {
    group = "basics custom testserver"
    description = "Copies all modules to the test server"
    val copyAllModulesTask = this
    subprojects.forEach { module ->
        module.tasks.withType(CopyModule::class).forEach { copyModuleTask ->
            copyAllModulesTask.dependsOn(copyModuleTask)
        }
    }
}

tasks.register("printVersion") {
    doLast {
        println(version)
    }
}

tasks {
    runServer {

        group = "basics"
        description = "Runs the server with the Basics plugin and all modules"

        dependsOn(distribution)

        val distFolder = project.rootDir.resolve("build/dist/basics-${project.version}")
        val serverFolder = project.rootDir.resolve("run")
        val pluginsFolder = serverFolder.resolve("plugins")
        val basicsFolder = pluginsFolder.resolve("Basics")

        doFirst {
            basicsFolder.deleteRecursivelyOrThrow()
            distFolder.copyRecursively(pluginsFolder, true)
        }
        minecraftVersion("1.20.4")
    }
}

runPaper {
    disablePluginJarDetection()
}
