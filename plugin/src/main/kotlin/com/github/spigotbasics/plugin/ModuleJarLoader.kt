package com.github.spigotbasics.plugin

import com.github.spigotbasics.core.module.BasicsModule
import com.github.spigotbasics.core.module.ModuleInfo
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File
import java.io.FileFilter
import java.net.URLClassLoader
import java.util.jar.JarFile
import kotlin.reflect.KClass

class ModuleJarLoader(val file: File, val parentClassLoader: ClassLoader?) {

    val moduleInfo: ModuleInfo = loadModuleInfo()
    val mainClass: KClass<out BasicsModule> = loadMainClass()

    private fun loadMainClass(): KClass<out BasicsModule> {
        val classLoader = URLClassLoader.newInstance(arrayOf(file.toURI().toURL()), parentClassLoader)
        return classLoader.loadClass(moduleInfo.mainClass).kotlin as KClass<out BasicsModule>
        //return Class.forName(moduleInfo.mainClass).kotlin as KClass<out BasicsModule>
    }

    private fun loadModuleInfo(): ModuleInfo {
        val jar = JarFile(file)
        val moduleInfoZipEntry = jar.getEntry(MODULE_INFO_FILE_NAME)
            ?: throw IllegalArgumentException("does not contain $MODULE_INFO_FILE_NAME")
        val moduleInfoInputStream = jar.getInputStream(moduleInfoZipEntry)
        val moduleInfoYaml = YamlConfiguration.loadConfiguration(moduleInfoInputStream.reader())
        val mainClass = moduleInfoYaml.getString("main-class") ?: throw IllegalArgumentException("main-class not set")
        val name = moduleInfoYaml.getString("name") ?: throw IllegalArgumentException("name not set")
        return ModuleInfo(mainClass, name)
    }



    companion object {
        val jarFileFilter = FileFilter {
            it.isFile && it.name.lowercase().endsWith(".jar")
        }
        const val MODULE_INFO_FILE_NAME = "basics-module.yml"
    }


}