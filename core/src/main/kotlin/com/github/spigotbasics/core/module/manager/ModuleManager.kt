package com.github.spigotbasics.core.module.manager

import com.github.spigotbasics.core.BasicsPlugin
import com.github.spigotbasics.core.exceptions.InvalidModuleException
import com.github.spigotbasics.core.exceptions.ModuleAlreadyLoadedException
import com.github.spigotbasics.core.logger.BasicsLoggerFactory
import com.github.spigotbasics.core.module.BasicsModule
import com.github.spigotbasics.core.module.loader.ModuleJarFileFilter
import com.github.spigotbasics.core.module.loader.ModuleLoader
import org.bukkit.Server
import org.jetbrains.annotations.Blocking
import java.io.File
import java.io.FileNotFoundException
import java.lang.Thread.sleep
import java.util.concurrent.CompletableFuture
import java.util.logging.Level

class ModuleManager constructor(
    private val plugin: BasicsPlugin,
    private val server: Server,
    val modulesDirectory: File
) {

    private val logger = BasicsLoggerFactory.getCoreLogger(ModuleLoader::class)

    private val myLoadedModules: MutableList<BasicsModule> = mutableListOf()

    val loadedModules: List<BasicsModule>
        get() = myLoadedModules.sortedBy { it.info.name }

    val enabledModules: List<BasicsModule>
        get() = myLoadedModules.filter { it.isEnabled() }

    val disabledModules: List<BasicsModule>
        get() = myLoadedModules.filter { !it.isEnabled() }

    init {
        if (!modulesDirectory.isDirectory()) {
            modulesDirectory.mkdirs()
        }
    }

    fun getModule(name: String): BasicsModule? = myLoadedModules.find { it.info.name == name }

    fun loadAllModulesFromModulesFolder() {
        val moduleFiles = modulesDirectory.listFiles(ModuleJarFileFilter)
            ?: throw FileNotFoundException("Modules directory ${modulesDirectory.absolutePath} not found")
        for (moduleFile in moduleFiles) {
            try {
                loadModuleFromFile(moduleFile)
            } catch (_: ModuleAlreadyLoadedException) {

            }
        }
    }

    fun enableAllLoadedModules() {
        for (module in myLoadedModules) {
            enableModule(module, false)
        }
    }

    fun loadAndEnableAllModulesFromModulesFolder() {
        loadAllModulesFromModulesFolder()
        enableAllLoadedModules()
    }

    // TODO: Switch back to this when issues happen
    @Throws(ModuleAlreadyLoadedException::class, InvalidModuleException::class)
    fun loadModuleFromFile(moduleFile: File): Result<BasicsModule> {
        logger.info("Loading module ${moduleFile.absolutePath}")
        val loader = try {
            ModuleLoader(plugin, server, moduleFile)
        } catch (e: InvalidModuleException) {
            logger.log(Level.SEVERE, "Failed to load module ${moduleFile.absolutePath}", e)
            return Result.failure(e)
        }
        val info = loader.info

        if (getModule(info.name) != null) {
            throw ModuleAlreadyLoadedException(info)
        }

        val module = try {
            loader.createInstance()
        } catch (e: InvalidModuleException) {
            logger.log(Level.SEVERE, "Failed to instantiate module ${info.nameAndVersion}", e)
            return Result.failure(e)
        }

        // TODO: Initialize all classes here, then close the loader

        //loader.close() // TODO: Remove this when issues happen

        myLoadedModules += module
        return Result.success(module)
    }

//    @Throws(ModuleAlreadyLoadedException::class, InvalidModuleException::class)
//    fun loadModuleFromFile(moduleFile: File): Result<BasicsModule> {
//        ModuleLoader(plugin, moduleFile).use { loader ->
//            val info = loader.info
//
//            if (getModule(info.name) != null) {
//                throw ModuleAlreadyLoadedException(info)
//            }
//
//            val module = try {
//                loader.createInstance()
//            } catch (e: InvalidModuleException) {
//                logger.log(Level.SEVERE, "Failed to instantiate module ${info.nameAndVersion}", e)
//                return Result.failure(e)
//            }
//
//            myLoadedModules += module
//            return Result.success(module)
//        }
//    }

    fun enableModule(module: BasicsModule, reloadConfig: Boolean) {
        logger.info("Enabling module ${module.info.nameAndVersion}")
        //if(enabledModules.contains(module)) {
        if (module.isEnabled()) {
            error("Module ${module.info.name} is already enabled")
        }
        module.enable(reloadConfig)
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
        module.loadAllOnlinePlayerData().whenComplete { _, e ->
            if (e != null) {
                logger.log(Level.SEVERE, "Failed to load all online player data for module ${module.info.name}", e)
            }
            logger.info("Enabled module ${module.info.nameAndVersion}")
        }
        //enabledModules.add(module)
    }

    fun disableModule(module: BasicsModule): CompletableFuture<Void?> {
        logger.info("Disabling module ${module.info.nameAndVersion}")
        //if(!enabledModules.contains(module)) {
        if (!module.isEnabled()) {
            error("Module ${module.info.name} is not enabled")
        }

        try {
            module.onDisable()
        } catch (e: Exception) {
            logger.log(Level.SEVERE, "Error while disabling module ${module.info.name}", e)
        }

        return module.saveAndForgetAllOnlinePlayerData().whenComplete { _, e ->
            if (e != null) {
                logger.log(
                    Level.SEVERE,
                    "Failed to save and forget all online player data for module ${module.info.name}",
                    e
                )
            }
        }.whenComplete { _, _ ->
            module.disable().get()
            logger.info("Disabled module ${module.info.nameAndVersion}")
        }
    }

    fun unloadModule(module: BasicsModule, forceGc: Boolean = false) {
        logger.info("Unloading module ${module.info.nameAndVersion}")
        if (module.isEnabled()) {
            throw IllegalArgumentException("Module ${module.info.name} is enabled, hence can't be unloaded")
        }
        myLoadedModules.remove(module)
        module.moduleClassLoader.close()

        if (forceGc) {
            forceGc()
        }

    }

    private fun forceGc() {
        System.runFinalization()
        System.gc()
        Thread {
            sleep(1000)
            System.runFinalization()
            System.gc()
        }.start()

    }

    fun disableAllModules(): CompletableFuture<Void?> {
        val futures = mutableListOf<CompletableFuture<Void?>>()
        for (module in enabledModules.toList()) {
            futures += disableModule(module)
        }
        return CompletableFuture.allOf(*futures.toTypedArray())
    }

    @Blocking
    fun disableAndUnloadAllModules() { // TODO: disable/unload should happen after each other, instead of first disabling all modules, then unloading all modules
        disableAllModules().get()
        for (module in myLoadedModules.toList()) {
            unloadModule(module, false)
        }
        forceGc()
    }


}
