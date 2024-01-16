package com.github.spigotbasics.plugin.module

import com.github.spigotbasics.core.BasicsPlugin
import com.github.spigotbasics.core.EitherResult
import com.github.spigotbasics.core.module.*
import java.io.File
import kotlin.reflect.KClass
import kotlin.reflect.full.createType

class ModuleManagerImpl(val plugin: BasicsPlugin): ModuleManager {

    private val logger = plugin.logger

    override val loadedModules: MutableList<BasicsModule> = ArrayList()
    override val enabledModules: MutableList<BasicsModule> = ArrayList()


    // Todo: Must also remove the module from enabledModules
    override fun disableModule(module: BasicsModule) {
        try {
            logger.info("Disabling module ${module.info.nameAndVersion}")
            module.disable()
        } catch (e: Exception) {
            logger.warning("Failed to disable module ${module.info.nameAndVersion}: ${e.message}")
        }
    }

    override fun enableModule(module: BasicsModule) {
        try {
            logger.info("Enabling module ${module.info.nameAndVersion}")
            module.enable()
            enabledModules.add(module)
        } catch (e: Exception) {
            logger.warning("Failed to enable module ${module.info.nameAndVersion}: ${e.message}")
            disableModule(module)
        }
    }

    override fun loadModule(pluginFile: File): EitherResult<BasicsModule, Exception> {
        try {
            logger.info("Loading module ${pluginFile.name}...")
            val uncleClassLoader = this.javaClass.classLoader
            val jarFileLoader = ModuleJarFileLoader(pluginFile, uncleClassLoader)
            val module = createModuleFromClass(jarFileLoader.mainClass, jarFileLoader.moduleInfo)
            module.load()
            loadedModules.add(module)
            return EitherResult.Success(module)
        } catch (e: Exception) {
            return EitherResult.Failed(e)
        }
    }

    fun createModuleFromClass(mainClass: KClass<out BasicsModule>, info: ModuleInfo): BasicsModule {
        val constructor = mainClass.constructors.stream()
            .filter { it.parameters.size == 2
                    && it.parameters[0].type == BasicsPlugin::class.createType()
                    && it.parameters[1].type == ModuleInfo::class.createType()
            }
            .findFirst()
            .orElseThrow {
                InvalidModuleException("Cannot find constructor for BasicsModule ${mainClass.qualifiedName}")
            }
        return constructor.call(plugin, info)
    }

    fun loadModulesFromFolder(moduleFolder: File) {
        moduleFolder.listFiles(ModuleLoader.jarFileFilter)?.forEach { jarFile ->
            val result = loadModule(jarFile)
            if(result is EitherResult.Failed) {
                logger.warning("Failed to load module ${jarFile.name}: ${result.value}")
                result.value.printStackTrace()
            }
        }
    }

    fun enableAllModules() {
        loadedModules.forEach { module ->
            enableModule(module)
        }
    }

    fun disableAllModules() {
        val it = enabledModules.iterator()
        while (it.hasNext()) {
            val module = it.next()
            disableModule(module)
            it.remove()
        }
    }


}