package com.github.spigotbasics.plugin.module

import com.github.spigotbasics.core.BasicsPlugin
import com.github.spigotbasics.core.module.*
import java.io.File
import java.util.concurrent.CopyOnWriteArrayList
import java.util.logging.Level
import kotlin.reflect.KClass
import kotlin.reflect.full.createType

class ModuleManagerImpl(val plugin: BasicsPlugin) : ModuleManager {

    private val logger = plugin.logger

    override val loadedModules: MutableList<BasicsModule> = ArrayList()
    override val enabledModules: MutableList<BasicsModule> = CopyOnWriteArrayList()


    override fun disableModule(module: BasicsModule): Result<Unit> {
        try {
            logger.info("Disabling module ${module.info.nameAndVersion}")
            module.disable()
            return Result.success(Unit)
        } catch (e: Exception) {
            logger.log(Level.SEVERE, "Failed to disable module ${module.info.nameAndVersion}", e)
            return Result.failure(e)
        } finally {
            enabledModules.remove(module)
        }
    }

    override fun enableModule(module: BasicsModule): Result<Unit> {
        try {
            logger.info("Enabling module ${module.info.nameAndVersion}")
            module.enable()
            enabledModules.add(module)
            return Result.success(Unit)
        } catch (e: Exception) {
            logger.log(Level.SEVERE, "Failed to enable module ${module.info.nameAndVersion}", e)
            disableModule(module)
            return Result.failure(e)
        }
    }

    override fun loadModule(pluginFile: File): Result<BasicsModule> {
        try {
            logger.info("Loading module ${pluginFile.name}...")
            val uncleClassLoader = this.javaClass.classLoader
            val jarFileLoader = ModuleJarFileLoader(pluginFile.toURI(), uncleClassLoader)
            val module = createModuleInstance(jarFileLoader.mainClass, jarFileLoader.moduleInfo).getOrElse { throw it }
            module.load()
            loadedModules.add(module)
            return Result.success(module)
        } catch (e: Exception) {
            logger.log(Level.SEVERE, "Failed to load module ${pluginFile.name}", e)
            return Result.failure(e)
        }
    }

    fun createModuleInstance(mainClass: KClass<out BasicsModule>, info: ModuleInfo): Result<BasicsModule> {
        try {
            val constructor = mainClass.constructors.stream()
                .filter {
                    it.parameters.size == 2
                            && it.parameters[0].type == BasicsPlugin::class.createType()
                            && it.parameters[1].type == ModuleInfo::class.createType()
                }
                .findFirst()
                .orElseThrow {
                    InvalidModuleException("Cannot find constructor for BasicsModule ${mainClass.qualifiedName}")
                }
            return Result.success(constructor.call(plugin, info))
        } catch (e: Exception) {
            logger.log(Level.SEVERE, "Failed to create module from class ${mainClass.qualifiedName}", e)
            return Result.failure(e)
        }
    }

    fun loadModulesFromFolder(moduleFolder: File): Boolean {
        if (!moduleFolder.isDirectory) {
            logger.log(Level.SEVERE, "Failed to load modules from folder ${moduleFolder.absolutePath}: Not a directory")
            return false
        }

        var success = true
        moduleFolder.listFiles(ModuleLoader.jarFileFilter)?.forEach { jarFile ->
            val result = loadModule(jarFile)
            if(result.isFailure) {
                success = false
            }
        }

        return success
    }

    fun enableAllModules() {
        loadedModules.forEach { module ->
            enableModule(module)
        }
    }

    fun disableAllModules() {
        enabledModules.forEach { module ->
            disableModule(module)
        }
    }


}