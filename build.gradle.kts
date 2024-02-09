import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

version = "0.0.2-SNAPSHOT"

plugins {
    base
    // id("basics.kotlin-conventions")
    id("com.github.johnrengelman.shadow") apply false
    id("org.jlleitschuh.gradle.ktlint") version "12.1.0"
}

allprojects {
    repositories {
        mavenCentral()
    }
    tasks.withType<ShadowJar> {
        archiveVersion = ""
        archiveClassifier = "shaded"
    }
}

subprojects {
    apply(plugin = "org.jlleitschuh.gradle.ktlint")
    version = rootProject.version
}

tasks.register("copyAllToTestServer") {
    group = "basics"
    description = "Copies the plugin and all modules to the test server"
    dependsOn("plugin:copyPluginToTestServer")
    dependsOn("copyAllModulesToTestServer")
}

tasks.register<Copy>("distribution") {
    group = "basics"
    description = "Bundle the plugin and all modules into a single directory."

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
    group = "basics-test"
    description = "Copies all modules to the test server"
    val copyAllModulesTask = this
    subprojects.forEach { module ->
        module.tasks.withType(CopyModule::class).forEach { copyModuleTask ->
            copyAllModulesTask.dependsOn(copyModuleTask)
        }
    }
}

tasks.register("createModule", CreateModule::class) {
    group = "basics"
    description = "Creates a new module"
}

tasks.register("printVersion") {
    doLast {
        println(version)
    }
}