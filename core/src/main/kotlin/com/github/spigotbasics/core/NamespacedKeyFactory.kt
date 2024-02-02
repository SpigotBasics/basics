package com.github.spigotbasics.core

import com.github.spigotbasics.core.module.BasicsModule
import com.github.spigotbasics.core.module.ModuleInfo
import org.bukkit.NamespacedKey
import org.bukkit.plugin.Plugin

class NamespacedKeyFactory(private val plugin: Plugin, private val namespace: String) {

    companion object {
        fun forModule(plugin: Plugin, module: ModuleInfo): NamespacedKeyFactory {
            return NamespacedKeyFactory(plugin, "module/" + module.name)
        }

        fun forCore(plugin: Plugin): NamespacedKeyFactory {
            return NamespacedKeyFactory(plugin,"core")
        }

    }

    fun create(key: String): NamespacedKey {
        return NamespacedKey(plugin, "$namespace/$key")
    }

}