import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

/**
 * Used for all modules
 */

plugins {
    id("basics.kotlin-conventions")
    id("basics.dependency.spigot-api")
    `java-library`
    id("com.github.johnrengelman.shadow")
}

apply(plugin = "com.github.johnrengelman.shadow")

dependencies {
    compileOnly(project(":core"))
}

tasks.build {
    dependsOn("shadowJar")
}

tasks.getByName("shadowJar", ShadowJar::class).apply {
    archiveClassifier = "shaded"
    dependencies {
        exclude(dependency("org.jetbrains.kotlin:kotlin-stdlib"))
        exclude(dependency("org.jetbrains:annotations"))
    }
}

tasks.processResources {
    filesMatching("basics-module.yml") {
        expand(mapOf(
            "moduleVersion" to project.version,
            "moduleName" to project.name
        ))
    }
}

val moduleName = project.name
tasks.register("copyModule${moduleName.pascalCase()}ToTestServer", CopyModule::class) {
    group  = "basics-test"
    description = "Copies the ${moduleName} module to the test server"
    from(tasks.getByName("shadowJar", ShadowJar::class).archiveFile)
    into(getServerModulesDirectory())
}