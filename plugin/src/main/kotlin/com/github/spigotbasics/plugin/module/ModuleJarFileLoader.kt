package com.github.spigotbasics.plugin.module

import com.github.spigotbasics.core.module.BasicsModule
import com.github.spigotbasics.core.module.InvalidModuleException
import com.github.spigotbasics.core.module.ModuleInfo
import com.github.spigotbasics.core.module.ModuleLoader
import com.github.spigotbasics.core.module.ModuleLoader.Companion.MODULE_INFO_FILE_NAME
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File
import java.net.URLClassLoader
import java.util.jar.JarFile
import kotlin.reflect.KClass
import kotlin.reflect.full.isSuperclassOf

/**
 * Module jar file loader
 *
 * @property file the jar file to load
 * @property parentClassLoader the parent class loader
 * @constructor Create empty Module jar file loader
 */
class ModuleJarFileLoader(val file: File, val parentClassLoader: ClassLoader?): ModuleLoader {

    val moduleInfo: ModuleInfo = loadModuleInfo()
    val mainClass: KClass<out BasicsModule> = loadMainClass()

    override fun loadMainClass(): KClass<out BasicsModule> {
        val classLoader = URLClassLoader.newInstance(arrayOf(file.toURI().toURL()), parentClassLoader)
        val moduleClass = classLoader.loadClass(moduleInfo.mainClass).kotlin
        if (!BasicsModule::class.isSuperclassOf(moduleClass)) {
            throw InvalidModuleException("main class ${moduleClass.qualifiedName} must be a subclass of BasicsModule")
        }
        @Suppress("UNCHECKED_CAST")
        return moduleClass as KClass<out BasicsModule>
    }

    override fun loadModuleInfo(): ModuleInfo {
        val jar = JarFile(file)
        val moduleInfoZipEntry = jar.getEntry(MODULE_INFO_FILE_NAME)
            ?: throw IllegalArgumentException("does not contain $MODULE_INFO_FILE_NAME")
        val moduleInfoInputStream = jar.getInputStream(moduleInfoZipEntry)
        val moduleInfoYaml = YamlConfiguration.loadConfiguration(moduleInfoInputStream.reader())
        val mainClass = moduleInfoYaml.getString("main-class") ?: throw IllegalArgumentException("main-class not set")
        val name = moduleInfoYaml.getString("name") ?: throw IllegalArgumentException("name not set")
        val version = moduleInfoYaml.getString("version") ?: throw IllegalArgumentException("version not set")
        return ModuleInfo(mainClass, name, version)
    }

}