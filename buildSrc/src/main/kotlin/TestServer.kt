import org.gradle.api.Project
import java.io.File

fun Project.getServerDirectory(): File {
    val testServerPath = project.providers.gradleProperty("testserver.path").orNull
        ?: error("Path to test server not set! Set gradle property testserver.path to the path of your test server (e.g. -Ptestserver.path=/Users/mfnalex/mctest or using gradle.properties in your home directory: https://docs.gradle.org/current/userguide/build_environment.html#sec:gradle_configuration_properties)")
    val testServerDir = File(testServerPath)
    if (!testServerDir.isDirectory) {
        error("Path to test server does not exist or is not a directory: $testServerPath")
    }
    return testServerDir
}

fun Project.getServerPluginsDirectory(): File {
    return File(getServerDirectory(), "plugins")
}

fun Project.getServerModulesDirectory(): File {
    return File(getServerPluginsDirectory(), "Basics/modules")
}