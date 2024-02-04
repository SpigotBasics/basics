import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import java.io.File
import java.nio.file.*
import java.nio.file.attribute.BasicFileAttributes

val skeletonModuleName = "_skeleton"
val skeletonJavaModuleName = "_skeleton-java"
val languageInputPrompt = "Which language do you want to use? [kotlin, java] : "
val moduleInputPrompt = "Enter module name ( ${moduleNameRegex.pattern} ) : "

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

    fun copySkeletonTo(skeleton: String, target: File) {
        val skeleton = File(project.projectDir, skeleton)
        skeleton.copyRecursively(target)
    }

    fun replaceInFilesAndFilenames(rootDirectory: File, replacements: Map<String, String>) {
        if (!rootDirectory.exists() || !rootDirectory.isDirectory) {
            println("The provided path does not exist or is not a directory.")
            return
        }

        // Separate the processing into two phases: content replacement and renaming
        try {
            // Phase 1: Replace contents without renaming files/directories yet
            Files.walkFileTree(rootDirectory.toPath(), object : SimpleFileVisitor<Path>() {
                override fun visitFile(file: Path, attrs: BasicFileAttributes): FileVisitResult {
                    replaceInFileContent(file.toFile(), replacements)
                    return FileVisitResult.CONTINUE
                }
            })

            // Phase 2: Collect and rename files/directories, starting from the deepest part
            val pathsToRename = mutableListOf<Path>()
            Files.walkFileTree(rootDirectory.toPath(), object : SimpleFileVisitor<Path>() {
                override fun preVisitDirectory(dir: Path, attrs: BasicFileAttributes): FileVisitResult {
                    pathsToRename.add(dir)
                    return FileVisitResult.CONTINUE
                }

                override fun visitFile(file: Path, attrs: BasicFileAttributes): FileVisitResult {
                    pathsToRename.add(file)
                    return FileVisitResult.CONTINUE
                }
            })

            // Reverse the list to rename deepest files/directories first
            pathsToRename.reverse()
            for (path in pathsToRename) {
                replaceInFilename(path.toFile(), replacements)
            }
        } catch (e: Exception) {
            println("An error occurred while processing the directory: ${e.message}")
        }
    }

    fun replaceInFileContent(file: File, replacements: Map<String, String>) {
        try {
            var content = file.readText()
            replacements.forEach { (key, value) ->
                content = content.replace("%{$key}%", value)
            }
            file.writeText(content)
        } catch (e: Exception) {
            println("Failed to replace content in file: ${file.path}. Error: ${e.message}")
        }
    }

    fun replaceInFilename(file: File, replacements: Map<String, String>): String {
        var newName = file.name
        replacements.forEach { (key, value) ->
            newName = newName.replace("%{$key}%", value)
        }
        if (newName != file.name) {
            val newFile = File(file.parent, newName)
            if (file.renameTo(newFile)) {
                return newFile.path
            } else {
                println("Failed to rename file: ${file.path} to $newName")
            }
        }
        return file.path
    }

}