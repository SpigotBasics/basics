package com.github.spigotbasics.core.module

import co.aikar.commands.PaperCommandManager
import com.github.spigotbasics.core.BasicsPlugin

import com.github.spigotbasics.core.scheduler.BasicsScheduler
import org.bukkit.configuration.file.FileConfiguration

import com.github.spigotbasics.core.config.SavedConfig
import com.github.spigotbasics.core.module.loader.ModuleJarClassLoader
import java.io.File

import java.util.logging.Logger

/**
 * Common interfaces to be used by all modules. Implementations require a public constructor that takes in a [BasicsPlugin] and a [ModuleInfo].
 *
 * @constructor Create empty Basics plugin
 */
interface BasicsModule {

    val plugin: BasicsPlugin
    val commandManager: PaperCommandManager
    val moduleClassLoader: ModuleJarClassLoader
    val moduleFile: File

    /**
     * Info about this module
     */
    val info: ModuleInfo

    /**
     * This module's config
     */
    val config: SavedConfig

    /**
     * This module's Logger
     */
    val logger: Logger

    /**
     * Modules scheduler
     */
    val scheduler: BasicsScheduler

    /**
     * Called when the module is enabled
     *
     */
    fun onEnable() {

    }

    /**
     * Called when the module is disabled
     *
     */
    fun onDisable() {

    }

    fun enable()
    fun disable()

    fun isEnabled(): Boolean

//    /**
//     * Called when the module is loaded
//     *
//     */
//    fun onLoad() {
//
//    }

}
