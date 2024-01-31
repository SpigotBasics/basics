package com.github.spigotbasics.core.util

import org.bukkit.plugin.java.JavaPlugin
import java.lang.reflect.Field

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
object ClassLoaderFix {

    private val isSuperEnabledField: Field? = try {
        JavaPlugin::class.java.getDeclaredField("isEnabled").apply { isAccessible = true }
    } catch (t: Throwable) {
        null
    }

    /**
     * Fixes NoClassDefFoundError when AbstractBasicsModule loops over storages in onDisable() when a module has not
     * created any.
     */
    fun trickOnEnable() {
        emptyList<Void?>().iterator().apply { }
    }


    /**
     * Sets JavaPlugin#isEnabled. Even though I checked Bukkit's classloading code, and as of
     * 1.20.4 I didn't find any reference of isEnabled being checked there, I remember having seen it there somewhere
     * in the findClass method. Maybe it's a Paper thing.
     *
     * The workaround is to set isEnabled to true in onDisable() and then set it back to false at the end of onDisable().
     */
    fun setSuperEnabled(plugin: JavaPlugin, enabled: Boolean) { // This is called in onDisable()
        isSuperEnabledField?.set(plugin, enabled)
    }
}