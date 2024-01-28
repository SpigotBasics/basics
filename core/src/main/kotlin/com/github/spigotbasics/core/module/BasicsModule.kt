package com.github.spigotbasics.core.module

import com.github.spigotbasics.core.BasicsPlugin
import com.github.spigotbasics.core.command.BasicsCommandManager

import com.github.spigotbasics.core.scheduler.BasicsScheduler

import com.github.spigotbasics.core.config.SavedConfig
import com.github.spigotbasics.core.event.BasicsEventBus
import com.github.spigotbasics.core.messages.MessageFactory
import com.github.spigotbasics.core.messages.TagResolverFactory
import com.github.spigotbasics.core.module.loader.ModuleJarClassLoader
import net.kyori.adventure.platform.bukkit.BukkitAudiences
import java.io.File

import java.util.logging.Logger

/**
 * Common interfaces to be used by all modules. Implementations require a public constructor that takes in a [BasicsPlugin] and a [ModuleInfo].
 *
 * @constructor Create empty Basics plugin
 */
interface BasicsModule {

    val plugin: BasicsPlugin
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
     * Message Factory
     */
    val messageFactory: MessageFactory

    /**
     * Command manager
     */
    val commandManager: BasicsCommandManager

    /**
     * Event bus for registering events
     */
    val eventBus: BasicsEventBus

    val tagResolverFactory: TagResolverFactory

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

    fun enable(reloadConfig: Boolean)
    fun disable()

    fun isEnabled(): Boolean


    fun reloadConfig()

}
