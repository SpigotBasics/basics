package com.github.spigotbasics.core.module.loader

import com.github.spigotbasics.core.exceptions.ForbiddenFruitException
import com.github.spigotbasics.core.logger.BasicsLoggerFactory
import com.github.spigotbasics.core.module.BasicsModule
import org.bukkit.plugin.PluginManager
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scheduler.BukkitScheduler
import java.io.File
import java.net.URL
import java.net.URLClassLoader

class ModuleJarClassLoader(val file: File, parentLoader: ClassLoader) :
    URLClassLoader(arrayOf(file.toURI().toURL()), parentLoader) {
    private val logger = BasicsLoggerFactory.getCoreLogger(ModuleJarClassLoader::class)

    companion object {
        val FORBIDDEN_CLASSES =
            mapOf(
                BukkitScheduler::class to BasicsModule::scheduler,
                BukkitRunnable::class to BasicsModule::scheduler,
            ).map { (forbidden, replacement) ->
                forbidden.java.name to replacement
            }.toMap()

        val DANGEROUS_CLASSES =
            listOf(
                PluginManager::class,
            ).map { it.java.name }
    }

    override fun loadClass(
        name: String,
        resolve: Boolean,
    ): Class<*> {
        val replacement = FORBIDDEN_CLASSES[name]
        if (replacement != null) {
            throw ForbiddenFruitException(name, replacement)
        }

        val dangerous = DANGEROUS_CLASSES.any { name == it }
        if (dangerous) {
            logger.warning(
                "Module from file ${file.name} is accessing class \"$name\" which might be dangerous - consider " +
                    "exposing needed functionality directly to modules!",
            )
        }

        return super.loadClass(name, resolve)
    }

    override fun close() {
        super.close()
        Runtime.getRuntime().gc()
    }

    override fun getResource(name: String?): URL? {
        return findResource(name) // TODO: Maybe return super.getResource if findResource is null?
    }
}
