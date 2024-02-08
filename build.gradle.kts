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
    tasks.withType<ShadowJar> {
        archiveVersion = ""
        archiveClassifier = "shaded"
    }
}

subprojects {
    apply(plugin = "org.jlleitschuh.gradle.ktlint")
}

tasks.register("copyAllToTestServer") {
    group = "basics"
    description = "Copies the plugin and all modules to the test server"
    dependsOn("plugin:copyPluginToTestServer")
    dependsOn("copyAllModulesToTestServer")
}

tasks.register<Zip>("zipDistribution") {
    group = "basics"
    description = "Bundle the plugin and all modules into a single zip file."

    archiveFileName = "basics-$version.zip"
    destinationDirectory = file("build/dist")

    println(1)
    from(project(":plugin").tasks.getByName("shadowJar", ShadowJar::class).archiveFile)
    println(2)
    for (module in project(":modules").subprojects) {
        println(module.name)
        module.tasks.withType<ShadowJar>().forEach { shadowTask ->
            from(shadowTask.archiveFile) {
                into("Basics/modules")
            }
        }
//        from(module.tasks.getByName("shadowJar", ShadowJar::class).archiveFile) {
//            into("Basics/modules")
//        }
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
