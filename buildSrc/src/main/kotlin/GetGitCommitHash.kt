import org.gradle.api.Project
import java.io.File
import java.io.IOException

fun Project.getGitCommitHash(): String {
    return "git rev-parse --short HEAD".runCommand(rootDir)?.trim() ?: "unknown"
}


private fun String.runCommand(workingDir: File): String? = try {
    ProcessBuilder(*split(" ").toTypedArray())
        .directory(workingDir)
        .redirectOutput(ProcessBuilder.Redirect.PIPE)
        .redirectErrorStream(true)
        .start()
        .inputStream.bufferedReader().readText()
} catch (e: IOException) {
    e.printStackTrace()
    null
}