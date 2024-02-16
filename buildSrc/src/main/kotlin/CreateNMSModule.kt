import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import java.io.File

private const val nag = "DON'T FORGET TO UPDATE NMSAggregator.kt"
private val mcVersionRegex = Regex("""^\d+\.\d+(\.\d+)?${'$'}""")
private val nmsVersionRegex = Regex("""^v\d+_\d+_R\d+${'$'}""")

open class CreateNMSModule : DefaultTask() {

    @TaskAction
    fun createModule() {

        println("Enter details for old and new MC and NMS versions.")
        println()
        println("See here for NMS versions:")
        println(" - https://www.spigotmc.org/wiki/spigot-nms-and-minecraft-versions-1-16/")
        println()

        val oldVersion = readInput("Enter old version (e.g. 1.20.4): ") ?: error("Old version is required")

        if (!oldVersion.matches(mcVersionRegex)) {
            error("Invalid old version: $oldVersion")
        }

        val oldNmsVersion = readInput("Enter old NMS version (e.g. v1_20_R3): ") ?: error("Old NMS version is required")

        if (!oldNmsVersion.matches(nmsVersionRegex)) {
            error("Invalid old NMS version: $oldNmsVersion")
        }

        val newVersion = readInput("Enter new version (e.g. 1.20.5): ") ?: error("New version is required")

        if (!newVersion.matches(mcVersionRegex)) {
            error("Invalid new version: $newVersion")
        }

        val newNmsVersion = readInput("Enter new NMS version (e.g. v1_20_R4): ") ?: error("New NMS version is required")

        if (!newNmsVersion.matches(nmsVersionRegex)) {
            error("Invalid new NMS version: $newNmsVersion")
        }

        val oldVersionUnderscores = oldVersion.replace(".", "_")
        val newVersionUnderscores = newVersion.replace(".", "_")
        val newFolder = File(project.projectDir, "versions/$newVersion")
        val oldFolder = File(project.projectDir, "versions/$oldVersion")

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


        println(nag)
        System.err.println(nag)

    }

    private fun copyFolder(source: File, target: File) {
        target.mkdirs()
        source.resolve("build.gradle.kts").copyTo(target.resolve("build.gradle.kts"), false)
        source.resolve("src").copyRecursively(target.resolve("src"), true)
    }

}