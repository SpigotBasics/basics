import org.gradle.api.Project
import java.io.File

/**
 * Returns a proper group ID for this subproject, e.g. "com.github.spigotbasics.modules" for modules/tpa
 */
fun Project.getGroupId(): String {
    val groupId = "com.github.spigotbasics"
    val myParentDir = projectDir.parentFile
    if (rootDir == myParentDir) {
        return groupId
    }
    val relativeGroupId = myParentDir.relativeTo(rootDir).path.replace(File.separatorChar, '.')
    if (relativeGroupId.isEmpty()) {
        throw RuntimeException("Couldn't determine relative group ID for $projectDir")
    }
    return "${groupId}.${relativeGroupId}"
}