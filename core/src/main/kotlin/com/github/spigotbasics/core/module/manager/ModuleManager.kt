package com.github.spigotbasics.core.module.manager

import com.github.spigotbasics.core.BasicsLoggerFactory
import com.github.spigotbasics.core.BasicsPlugin
import com.github.spigotbasics.core.module.BasicsModule
import com.github.spigotbasics.core.module.InvalidModuleException
import com.github.spigotbasics.core.module.ModuleAlreadyLoadedException
import com.github.spigotbasics.core.module.loader.ModuleJarFileFilter
import com.github.spigotbasics.core.module.loader.ModuleLoader
import java.io.File
import java.io.FileNotFoundException
import java.util.logging.Level

class ModuleManager(val plugin: BasicsPlugin, val modulesDirectory: File) {

    private val logger = BasicsLoggerFactory.getCoreLogger(ModuleLoader::class)

    private val loadedModules: MutableList<BasicsModule> = mutableListOf()

    val modules: List<BasicsModule>
        get() = loadedModules.sortedBy { it.info.name } // TODO: Maybe have to call toList before calling sortedBy

    val enabledModules: List<BasicsModule>
        get() = loadedModules.filter { it.isEnabled() }

    val disabledModules: List<BasicsModule>
        get() = loadedModules.filter { !it.isEnabled() }

    init {
        if (!modulesDirectory.isDirectory()) {
            modulesDirectory.mkdirs()
        }
    }

    fun getModule(name: String): BasicsModule? = loadedModules.find { it.info.name == name }

    fun loadModules() {
        val moduleFiles = modulesDirectory.listFiles(ModuleJarFileFilter)
            ?: throw FileNotFoundException("Modules directory ${modulesDirectory.absolutePath} not found")
        for (moduleFile in moduleFiles) {
            try {
                loadModuleFromFile(moduleFile)
            } catch (_: ModuleAlreadyLoadedException) {

            }
        }
    }

    @Throws(ModuleAlreadyLoadedException::class, InvalidModuleException::class)
    private fun loadModuleFromFile(moduleFile: File) {
        val loader = try {
            ModuleLoader(plugin, moduleFile)
        } catch (e: InvalidModuleException) {
            logger.log(Level.SEVERE, "Failed to load module ${moduleFile.absolutePath}", e)
            return
        }
        val info = loader.info

        if(getModule(info.name) != null) {
            throw ModuleAlreadyLoadedException(info)
        }

        val module = try {
            loader.createInstance()
        } catch (e: InvalidModuleException) {
            logger.log(Level.SEVERE, "Failed to instantiate module ${info.nameAndVersion}", e)
            return
        }

        loadedModules += module
    }

    fun enableModule(module: BasicsModule) {
        //if(enabledModules.contains(module)) {
        if(module.isEnabled()) {
            error("Module ${module.info.name} is already enabled")
        }
        module.enable()
        try {
            module.onEnable()
        } catch (e: Exception) {
            logger.log(Level.SEVERE, "Failed to enable module ${module.info.name}", e)
            module.disable()
            try {
                module.onDisable()
            } catch (e: Exception) {
                logger.log(Level.SEVERE, "Failed to disable module ${module.info.name}", e)
            }
            return
        }
        //enabledModules.add(module)
    }

    fun disableModule(module: BasicsModule) {
        //if(!enabledModules.contains(module)) {
        if(!module.isEnabled()) {
            error("Module ${module.info.name} is not enabled")
        }
        module.disable()
        try {
            module.onDisable()
        } catch (e: Exception) {
            logger.log(Level.SEVERE, "Failed to disable module ${module.info.name}", e)
        }
        //enabledModules.remove(module)
    }

    fun unloadModule(module: BasicsModule) {
        if(module.isEnabled()) {
            throw IllegalArgumentException("Module ${module.info.name} is enabled, hence can't be unloaded")
        }
        loadedModules.remove(module)
        forceGc()

    }

    private fun forceGc() {
        System.runFinalization()
        System.gc()
    }


}