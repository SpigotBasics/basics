package com.github.spigotbasics.core

import com.github.spigotbasics.core.module.ModuleInfo
import org.bukkit.NamespacedKey
import org.bukkit.plugin.Plugin

/**
 * A factory for creating [NamespacedKey]s with an additional namespace in their key
 */
class NamespacedNamespacedKeyFactory(private val plugin: Plugin, private val namespace: String) {

    /**
     * Sorry I couldn't resist...
     */
    object NamespacedNamespacedKeyFactoryFactory{
        fun forModule(plugin: Plugin, module: ModuleInfo): NamespacedNamespacedKeyFactory {
            return NamespacedNamespacedKeyFactory(plugin, "module/" + module.name)
        }

        fun forCore(plugin: Plugin): NamespacedNamespacedKeyFactory {
            return NamespacedNamespacedKeyFactory(plugin,"core")
        }

    }

    fun create(key: String): NamespacedKey {
        return NamespacedKey(plugin, "$namespace/$key")
    }

}