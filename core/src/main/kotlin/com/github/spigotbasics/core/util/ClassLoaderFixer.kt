package com.github.spigotbasics.core.util

import com.github.spigotbasics.core.config.FixClassLoadingConfig
import com.github.spigotbasics.core.logger.BasicsLoggerFactory
import com.github.spigotbasics.core.module.AbstractBasicsModule
import com.github.spigotbasics.core.module.BasicsModule
import com.github.spigotbasics.core.module.manager.ModuleManager
import org.bukkit.plugin.java.JavaPlugin
import java.lang.reflect.Field
import java.util.jar.JarFile

/**
 * Methods to trick out the ClassLoader.
 *
 * The JavaPluginLoader removes the plugin's class loader from its List<PluginClassLoader> right after calling
 * onDisable() and closes it. This leads to issues when classes are needed to be first loaded in onDisable() - for
 * example, kotlin.collections.EmptyIterator is often needed for the first time in onDisable() when iterating over
 * module's storages, registered listeners, etc.
 *
 * I am confused as to why this is causing issues, because onDisable() is called blocking and hence should complete
 * before the classloader is actually killed - maybe it's only a Paper issue.
 *
 * I have also noticed that even using lambdas or other anonymous classes in onDisable() sometimes cause issues,
 * for example the NamespacedStorage class's shutdown() method sometimes throws NoClassDefFoundError for its
 * usages of forEach { }.
 *
 * Note that all this only sometimes causes problems. The following methods use some dirty tricks to make sure that
 * the classloader is not removed from the list and closed before onDisable() completes, and it loads classes
 * that are known to cause issues right during onEnable().
 */
class ClassLoaderFixer(
    private val pluginJarPath: String,
    private val config: FixClassLoadingConfig,
) {
    private val logger = BasicsLoggerFactory.getCoreLogger(this::class)

    private val isSuperEnabledField: Field? =
        try {
            JavaPlugin::class.java.getDeclaredField("isEnabled").apply { isAccessible = true }
        } catch (t: Throwable) {
            null
        }

    /**
     * Fixes NoClassDefFoundError when AbstractBasicsModule loops over storages in onDisable() when a module has not
     * created any.
     */
    fun trickOnEnable() {
        if (config.callIteratorOnEmptyList) {
            emptyList<Void?>().iterator().apply { }
        }

        if (config.abuseClassBasicsModule) {
            forceLoadClassesForEnclosingClass(BasicsModule::class.java)
        }

        if (config.abuseClassAbstractBasicsModule) {
            forceLoadClassesForEnclosingClass(AbstractBasicsModule::class.java)
        }

        if (config.abuseClassModuleManager) {
            forceLoadClassesForEnclosingClass(ModuleManager::class.java)
        }

        for (className in config.abuseClassesList) {
            try {
                val clazz = Class.forName(className)
                forceLoadClassesForEnclosingClass(clazz)
            } catch (_: Throwable) {
            }
        }

        if (config.loadAllClasses) {
            loadAllClassesInJar(pluginJarPath)
        }
    }

    /**
     * Sets JavaPlugin#isEnabled. Even though I checked Bukkit's classloading code, and as of
     * 1.20.4 I didn't find any reference of isEnabled being checked there, I remember having seen it there somewhere
     * in the findClass method. Maybe it's a Paper thing.
     *
     * The workaround is to set isEnabled to true in onDisable() and then set it back to false at the end of onDisable().
     */
    fun setSuperEnabled(
        plugin: JavaPlugin,
        enabled: Boolean,
    ) { // This is called in onDisable()
        if (config.setEnabledDuringOnDisable) {
            isSuperEnabledField?.set(plugin, enabled)
        }
    }

    /**
     * Tries to force load all classes that are referenced in the given class. This is needed to avoid NoClassDefFoundErrors
     * regarding lambdas and anonymous classes in CompletableFutures in onDisable().
     *
     * @param enclosingClass
     */
    fun forceLoadClassesForEnclosingClass(enclosingClass: Class<*>) {
        touchFields(enclosingClass)
        touchMethods(enclosingClass)
        enclosingClass.classes.forEach { forceLoadClassesForEnclosingClass(it) }
    }

    private fun touchMethods(enclosingClass: Class<*>) {
        for (method in enclosingClass.declaredMethods) {
            if (method.parameterCount == 0 && method.returnType != Void.TYPE) {
                try {
                    method.isAccessible = true
                    @Suppress("UNUSED_VARIABLE")
                    val value = method.invoke(null)
                } catch (_: Throwable) {
                }
            }
        }
    }

    private fun touchFields(enclosingClass: Class<*>) {
        for (field in enclosingClass.declaredFields) {
            try {
                field.isAccessible = true
                @Suppress("UNUSED_VARIABLE")
                val value = field[null]
            } catch (_: Throwable) {
            }
        }
    }

    fun loadAllClassesInJar(jarFilePath: String) {
        listClassNamesInJar(jarFilePath).forEach {
            try {
                logger.debug(998, "Loading class: $it")
                Class.forName(it, false, this::class.java.classLoader)
            } catch (e: Throwable) {
                logger.debug(999, "Failed to load class: $it - ${e.message}")
            }
        }
    }

    private fun listClassNamesInJar(jarFilePath: String): List<String> {
        val jarFile = JarFile(jarFilePath)
        logger.debug(10, "My jar file is $jarFile - $jarFilePath")

        return jarFile.entries().asSequence()
            .filter { it.name.endsWith(".class") }
            .map { entry ->
                entry.name.replace('/', '.').removeSuffix(".class")
            }.toList().apply {
                if (size == 0) {
                    logger.warning("No classes found in jar file: $jarFilePath")
                }
            }
    }
}
