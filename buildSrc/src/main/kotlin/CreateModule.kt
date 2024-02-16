import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import java.io.File

private const val skeletonModuleName = "_skeleton"
private const val skeletonJavaModuleName = "_skeleton-java"
private const val languageInputPrompt = "Which language do you want to use? [kotlin, java] : "
private val moduleInputPrompt = "Enter module name ( ${moduleNameRegex.pattern} ) : "

open class CreateModule : DefaultTask() {

    @TaskAction
    fun createModule() {
        var moduleName: String?
        var language: String?


        language = readInput(languageInputPrompt)
        if(language != "kotlin" && language != "java") {
            error("Invalid language: $language")
        }

        val skeleton = if(language == "kotlin") skeletonModuleName else skeletonJavaModuleName

        moduleName = readInput(moduleInputPrompt)
        if (!isValidModuleName(moduleName)) {
            error("Invalid module name: $moduleName - Enter `kotlin` or `java`")
        }

        moduleName !!

        val moduleLower = moduleName.pascalCase().lowercase()
        val moduleUpper = moduleName.pascalCase()

        val folder = File(project.projectDir, moduleName)
        if (folder.exists()) {
            error("Module $moduleName already exists")
        }

        println("Creating module $moduleName ...")

        println("Copying skeleton to $folder ...")
        copySkeletonTo(skeleton, folder)

        val replacements = mapOf(
            "module-name-lower" to moduleLower,
            "module-name-pascal" to moduleUpper,
            "module-name" to moduleName
        )

        println("Replacing placeholders in files and filenames ...")
        replaceInFilesAndFilenames(folder, replacements)

        println("Module $moduleName created successfully at:")
        println(folder.absolutePath)

    }

    private fun copySkeletonTo(skeleton: String, target: File) {
        val skeletonDir = File(project.projectDir, skeleton)
        skeletonDir.copyRecursively(target)
    }
}