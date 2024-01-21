package com.github.spigotbasics.core.module

import com.github.spigotbasics.core.BasicsPlugin
import com.github.spigotbasics.core.module.loader.ModuleJarClassLoader
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
    val info: ModuleInfo,
    val file: File,
    val classLoader: ModuleJarClassLoader
)
