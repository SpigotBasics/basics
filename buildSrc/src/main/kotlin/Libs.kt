import gradle.kotlin.dsl.accessors._e054d9723d982fdb55b1e388b8ab0cbf.versionCatalogs
import org.gradle.api.Project
import org.gradle.api.artifacts.MinimalExternalModuleDependency
import org.gradle.api.artifacts.VersionCatalog

fun Project.libs(): Libs {
    return Libs(versionCatalogs.named("libs"))
}

fun Project.libs(name: String): MinimalExternalModuleDependency {
    return libs().library(name)
}

class Libs(private val catalog: VersionCatalog) {

    fun library(name: String): MinimalExternalModuleDependency {
        return catalog.findLibrary(name).get().get()
    }


}