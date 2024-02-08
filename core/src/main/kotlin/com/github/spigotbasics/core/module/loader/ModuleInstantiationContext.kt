package com.github.spigotbasics.core.module.loader

import com.github.spigotbasics.core.BasicsPlugin
import com.github.spigotbasics.core.module.ModuleInfo
import org.bukkit.Server
import java.io.File

/**
 * Required information to create a module instance
 *
 * @property plugin the plugin instance
 * @property info the module info
 * @property file the module file
 * @property classLoader the class loader
 * @constructor Create empty Module context
 */
data class ModuleInstantiationContext(
    val plugin: BasicsPlugin,
    val server: Server,
    val info: ModuleInfo,
    val file: File,
    val classLoader: ModuleJarClassLoader,
)
