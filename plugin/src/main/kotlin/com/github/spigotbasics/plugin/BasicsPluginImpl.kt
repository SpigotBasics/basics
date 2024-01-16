package com.github.spigotbasics.plugin

import com.github.spigotbasics.core.BasicsPlugin
import com.github.spigotbasics.core.Result
import com.github.spigotbasics.core.extensions.placeholders
import com.github.spigotbasics.core.module.BasicsModule
import com.github.spigotbasics.core.module.ModuleInfo
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import java.util.jar.JarFile
import kotlin.reflect.KClass
import kotlin.reflect.full.createType

class BasicsPluginImpl : JavaPlugin(), BasicsPlugin {
    override val availableModules: MutableList<KClass<out BasicsModule>> = ArrayList()
    override val enabledModules: MutableList<BasicsModule> = ArrayList()
    override val moduleFolder = File(dataFolder, "modules")

    override fun onLoad() {
        if(!moduleFolder.isDirectory) {
            logger.info("Creating modules folder at ${moduleFolder.absolutePath}")
            moduleFolder.mkdirs()
        }
    }

    override fun loadModule(clazz: KClass<out BasicsModule>): Result<BasicsModule, Exception> {
        availableModules.add(clazz)
        val module: BasicsModule
        try {
            module = createModule(clazz)
        } catch (exception: Exception) {
            logger.severe("Could not load module class ${clazz.qualifiedName}")
            return Result.Failed(exception)
        }
        try {
            module.enable()
            enabledModules.add(module)
        } catch (exception: Exception) {
            logger.severe("Could not enable module ${module.name}")
            return Result.Failed(exception)
        }

        return Result.Success(module)
    }

    private fun createModule(clazz: KClass<out BasicsModule>): BasicsModule {
        val constructor = clazz.constructors.stream()
            .filter { it.parameters.size == 1 && it.parameters[0].type == BasicsPlugin::class.createType() }
            .findFirst()
            .orElseThrow {
                IllegalArgumentException("Cannot find constructor for BasicsModule ${clazz.qualifiedName}")
            }
        return constructor.call(this)
    }

    override fun onEnable() {
        logger.info(
            "Basics v%version% enabled! This plugin was written by %authors%."
                .placeholders(
                    "version" to description.version,
                    "authors" to "cool people"
                )
        )

        val jarFiles = moduleFolder.listFiles(ModuleJarLoader.jarFileFilter)
        if(jarFiles != null) {
            for(jarFile in jarFiles) {
                val jarLoader = ModuleJarLoader(jarFile, javaClass.classLoader)
                val moduleInfo = jarLoader.moduleInfo
                println("Found module ${moduleInfo.name} with main ${moduleInfo.mainClass}")
                println("Class: ${jarLoader.mainClass}")
            }
        }
    }

}