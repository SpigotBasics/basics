import org.gradle.api.tasks.Copy
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import java.io.File


fun getTestServerDir(): File {

    val testServerPath = providers.gradleProperty("testserver.path").orNull ?:
    error("Path to test server not set! Set gradle property testserver.path to the path of your test server (e.g. -Ptestserver.path=/Users/mfnalex/mctest or using gradle.properties in your home directory: https://docs.gradle.org/current/userguide/build_environment.html#sec:gradle_configuration_properties)")
    val testServerDir = File(testServerPath)
    if (!testServerDir.isDirectory) {
        error("Path to test server is not a directory: $testServerPath")
    }
    return testServerDir
}

val copyPlugin = project(":plugin").tasks.register("copyPluginToTestServer", Copy::class) {
    group = "testServer"
    description = "Copy plugin to test server"
    from(tasks.getByName("shadowJar", ShadowJar::class).archiveFile)
    into(getTestServerDir())
}
//
//project(":modules").tasks.register("copyModulesToTestServer", Copy::class) {
//    group = "testServer"
//    description = "Copy modules to test server"
//    /*project(":modules").*/subprojects.forEach {
//        from(it.tasks.getByName("shadowJar", ShadowJar::class).archiveFile)
//    }
//    into(File(getTestServerDir(), "plugins/Basics/modules"))
//}
//
//project.tasks.register("copyToTestServer") {
//    group = "testServer"
//    description = "Copy plugin and modules to test server"
//    dependsOn(copyPlugin)
//    dependsOn(copyModules)
//}