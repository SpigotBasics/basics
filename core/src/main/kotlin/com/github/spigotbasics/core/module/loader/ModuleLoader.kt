package com.github.spigotbasics.core.module.loader

import com.github.spigotbasics.core.MODULE_YML_FILE_NAME
import com.github.spigotbasics.core.module.BasicsModule
import com.github.spigotbasics.core.module.InvalidModuleException
import com.github.spigotbasics.core.module.ModuleInfo
import org.bukkit.configuration.InvalidConfigurationException
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File
import java.io.FileFilter
import java.io.FileNotFoundException
import java.io.IOException
import java.util.jar.JarFile
import kotlin.reflect.KClass

class ModuleLoader @Throws(InvalidModuleException::class) constructor(val file: File) {

    val path = file.absolutePath
    val jarFile: JarFile
    val moduleInfo: ModuleInfo
    val classLoader: ModuleJarClassLoader? = null

    init {
        if (!file.exists()) {
            throw InvalidModuleException("Module file $path does not exist")
        }
        if (!file.isFile) {
            throw InvalidModuleException("Module file $path is not a file")
        }
        if (!file.name.lowercase().endsWith(".jar")) {
            throw InvalidModuleException("Module file $path is not a jar file")
        }

        try {
            jarFile = JarFile(file)
        } catch (e: IOException) {
            throw InvalidModuleException("Failed to open module file $path as jar", e)
        }

        val moduleInfoEntry = jarFile.getEntry(MODULE_YML_FILE_NAME)
            ?: throw InvalidModuleException("Module file $path does not contain a $MODULE_YML_FILE_NAME file")

        val moduleInfoYamlString = jarFile.getInputStream(moduleInfoEntry).use { it.reader().readText() }
        val moduleInfoYaml = YamlConfiguration()

        try {
            moduleInfoYaml.loadFromString(moduleInfoYamlString)
        } catch (e: InvalidConfigurationException) {
            throw InvalidModuleException("Failed to parse $MODULE_YML_FILE_NAME file in $path", e)
        }

        try {
            moduleInfo = ModuleInfo.fromYaml(moduleInfoYaml)
        } catch (e: InvalidModuleException) {
            throw InvalidModuleException("Failed to parse $MODULE_YML_FILE_NAME file in $path", e)
        }
    }

//    fun getMainClass(): KClass<out BasicsModule> {
//        return Nothing
//    }



}