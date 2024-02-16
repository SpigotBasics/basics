import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

version = "git-${getGitCommitHash()}"

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
    group = "basics"
    description = "Copies the plugin and all modules to the test server"
    dependsOn("plugin:copyPluginToTestServer")
    dependsOn("copyAllModulesToTestServer")
}

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
    group = "basics-test"
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
