import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import java.io.File

val skeletonModuleName = "_skeleton"
val moduleInputPrompt = "Enter module name ( ${moduleNameRegex.pattern} ) : "

open class CreateModule : DefaultTask() {

    @TaskAction
    fun createModule() {
        var moduleName: String?

        moduleName = readInput(moduleInputPrompt)
        if (!isValidModuleName(moduleName)) {
            error("Invalid module name: $moduleName")
        }

        val moduleLower = moduleName!!.lowercase()
        val moduleUpper = moduleName.replaceFirstChar { it.uppercaseChar() }

        val folder = File(project.projectDir, moduleName)
        if (folder.exists()) {
            error("Module $moduleName already exists")
        }

        println("Creating module $moduleName ...")
        copySkeletonTo(folder)

        println("Adjusting basics-module.yml ...")
        replaceInFile(File(folder, "src/main/resources/basics-module.yml"), mapOf(
            "module-name" to moduleLower,
            "Module-name" to moduleUpper,
        ))

        println("Creating main class ...")
        createMain(folder, moduleUpper, moduleLower)
    }

    fun copySkeletonTo(target: File) {
        val skeleton = File(project.projectDir, skeletonModuleName)
        skeleton.copyRecursively(target)
    }

    fun replaceInFile(file: File, placeholders: Map<String,String>) {
        var content = file.readText()
        placeholders.forEach({ (key, value) ->
            content = content.replace("%{" + key + "}%", value)
        })
        file.writeText(content)
    }

    fun createMain(folder: File, nameUpper: String, nameLower: String) {
        val mainFile = File(folder, "src/main/kotlin/com/github/spigotbasics/modules/$nameLower/${nameUpper}Module.kt")
        mainFile.parentFile.mkdirs()

        mainFile.writeText("""
            package com.github.spigotbasics.modules.${nameLower}

            import cloud.commandframework.Description
            import com.github.spigotbasics.core.BasicsPlugin
            import com.github.spigotbasics.core.config.BasicsConfig
            import com.github.spigotbasics.core.module.AbstractBasicsModule
            import com.github.spigotbasics.core.module.ModuleInfo

            class ${nameUpper}Module(plugin: BasicsPlugin, info: ModuleInfo) : AbstractBasicsModule(plugin, info) {

                override val config = BasicsConfig() // TODO: Move this to AbstractBasicsModule

            }
        """)
    }

}