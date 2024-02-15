package com.github.spigotbasics.core

/**
 * The Basics core, this is the Basics equivalent of the [org.bukkit.Bukkit] class
 */
object Basics {
    private lateinit var basics: BasicsPlugin

    /**
     * Sets the plugin instance
     *
     * @param plugin The plugin instance
     */
    fun setPlugin(plugin: BasicsPlugin) {
        if (::basics.isInitialized) throw IllegalStateException("Plugin already set")
        this.basics = plugin
    }

    /**
     * Gets the Basics plugin instance
     *
     * @return The Basics plugin instance
     */
    val plugin get() = basics

    /**
     * Provides MiniMessage audiences
     */
    val audienceProvider get() = basics.audienceProvider

    /**
     * Facade to uniformly access Spigot and Paper specific features.
     */
    val facade get() = basics.facade

    /**
     * The folder where the plugin's modules are stored.
     */
    val moduleFolder get() = basics.moduleFolder

    /**
     * Manager responsible for handling the various modules of the plugin.
     */
    val moduleManager get() = basics.moduleManager

    /**
     * Factory for creating and getting tag resolvers for MiniMessage
     */
    val tagResolverFactory get() = basics.tagResolverFactory

    /**
     * Message factory
     */
    val messageFactory get() = basics.messageFactory

    /**
     * Manager for handling configuration and message files
     */
    val coreConfigManager get() = basics.coreConfigManager

    /**
     * Core Permissions
     */
    val permissions get() = basics.permissions

    /**
     * Messages used by the plugin itself or by more than one module.
     */
    val messages get() = basics.messages

    /**
     * Core configuration settings of the plugin.
     */
    val config get() = basics.config

    /**
     * Storage manager
     */
    val storageManager get() = basics.storageManager

    /**
     * Cache to lookup Names <> UUIDs
     */
    val corePlayerData get() = basics.corePlayerData

    /**
     * NMS Facade
     */
    val nms get() = basics.nms

    /**
     * Reloads the core configuration settings of the plugin.
     */
    fun reloadCoreConfig() {
        basics.reloadCoreConfig()
    }
}
