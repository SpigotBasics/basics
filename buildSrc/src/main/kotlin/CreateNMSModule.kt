import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import java.io.File

open class CreateNMSModule : DefaultTask() {

    private val mcVersionRegex = Regex("""^\d+\.\d+(\.\d+)?${'$'}""")
    private val nmsVersionRegex = Regex("""^v\d+_\d+_R\d+${'$'}""")

    @TaskAction
    fun createModule() {

        println("Enter details for old and new MC and NMS versions.")
        println("See here for NMS versions:")
        println(" - https://www.spigotmc.org/wiki/spigot-nms-and-minecraft-versions-1-16/")
        println()

        val oldVersion = readInput("Enter old version (e.g. 1.20.4): ") ?: error("Old version is required")
        val oldNmsVersion = readInput("Enter old NMS version (e.g. v1_20_R3): ") ?: error("Old NMS version is required")
        val newVersion = readInput("Enter new version (e.g. 1.20.5): ") ?: error("New version is required")
        val newNmsVersion = readInput("Enter new NMS version (e.g. v1_20_R4): ") ?: error("New NMS version is required")
        val oldVersionUnderscores = oldVersion.replace(".", "_")
        val newVersionUnderscores = newVersion.replace(".", "_")
        val newFolder = File(project.projectDir, "versions/$newVersion")
        val oldFolder = File(project.projectDir, "versions/$oldVersion")

        if (!oldVersion.matches(mcVersionRegex)) {
            error("Invalid old version: $oldVersion")
        }
        if (!newVersion.matches(mcVersionRegex)) {
            error("Invalid new version: $newVersion")
        }
        if (!oldNmsVersion.matches(nmsVersionRegex)) {
            error("Invalid old NMS version: $oldNmsVersion")
        }
        if (!newNmsVersion.matches(nmsVersionRegex)) {
            error("Invalid new NMS version: $newNmsVersion")
        }

        if (newFolder.exists()) {
            error("NMS Module $newNmsVersion already exists")
        }

        println("Copying NMS module $oldNmsVersion to $newNmsVersion ...")

        copyFolder(oldFolder, newFolder)

        val replacements = mapOf(
            oldNmsVersion to newNmsVersion,
            oldVersionUnderscores to newVersionUnderscores,
            oldVersion to newVersion
        )

        println("Replacing old version in files and filenames ...")
        replaceInFilesAndFilenames(newFolder, replacements)

        println("NMS Module $newVersion created successfully at:")
        println(newFolder.absolutePath)

    }

    private fun copyFolder(source: File, target: File) {
        source.copyRecursively(target)
    }

}