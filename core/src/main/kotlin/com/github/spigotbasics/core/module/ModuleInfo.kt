package com.github.spigotbasics.core.module

import com.github.spigotbasics.core.exceptions.InvalidModuleException
import com.github.spigotbasics.core.extensions.getAsStringList
import org.bukkit.configuration.file.YamlConfiguration

data class ModuleInfo(
    /**
     * Module's main class, must implement [BasicsModule]
     */
    val mainClass: String,
    /**
     * Module's name. Names of enabled plugins must be unique.
     */
    val name: String,
    /**
     * Module's version
     */
    val version: String,
    /**
     * Module's Description
     */
    val description: List<String>
) {
    /**
     * Name and version
     */
    val nameAndVersion = "$name v$version"

    companion object {

        /**
         * Creates a [ModuleInfo] from a [YamlConfiguration]
         */
        @Throws(InvalidModuleException::class)
        fun fromYaml(moduleInfoYaml: YamlConfiguration): ModuleInfo {
            val mainClass = moduleInfoYaml.getString("main-class")
                ?: throw InvalidModuleException("Module info does not contain a main-class")
            val name = moduleInfoYaml.getString("name")
                ?: throw InvalidModuleException("Module info does not contain a name")
            val version = moduleInfoYaml.getString("version")
                ?: throw InvalidModuleException("Module info does not contain a version")
            val description = moduleInfoYaml.getAsStringList("description")

            return ModuleInfo(mainClass, name, version, description)
        }
    }

}