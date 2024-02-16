import java.io.File
import java.nio.file.FileVisitResult
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.SimpleFileVisitor
import java.nio.file.attribute.BasicFileAttributes

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