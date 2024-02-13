package com.github.spigotbasics.core

import com.github.spigotbasics.core.config.CoreConfig
import com.github.spigotbasics.core.config.CoreConfigManager
import com.github.spigotbasics.core.messages.AudienceProvider
import com.github.spigotbasics.core.messages.CoreMessages
import com.github.spigotbasics.core.messages.MessageFactory
import com.github.spigotbasics.core.messages.tags.TagResolverFactory
import com.github.spigotbasics.core.module.manager.ModuleManager
import com.github.spigotbasics.core.playerdata.CorePlayerData
import com.github.spigotbasics.core.storage.StorageManager
import com.github.spigotbasics.pipe.SpigotPaperFacade
import java.io.File

/**
 * Represents the Basics Bukkit Plugin instance - this is equivalent to the Bukkit [org.bukkit.Server] class.
 */
interface BasicsPlugin {
    /**
     * Provides MiniMessage audiences
     */
    val audienceProvider: AudienceProvider

    /**
     * Facade to uniformly access Spigot and Paper specific features.
     */
    val facade: SpigotPaperFacade

    /**
     * The folder where the plugin's modules are stored.
     */
    val moduleFolder: File

    /**
     * Manager responsible for handling the various modules of the plugin.
     */
    val moduleManager: ModuleManager

    /**
     * Factory for creating and getting tag resolvers for MiniMessage
     */
    val tagResolverFactory: TagResolverFactory

    /**
     * Message factory
     */
    val messageFactory: MessageFactory

    /**
     * Manager for handling configuration and message files
     */
    val coreConfigManager: CoreConfigManager

    /**
     * Messages used by the plugin itself or by more than one module.
     */
    val messages: CoreMessages

    /**
     * Core configuration settings of the plugin.
     */
    val config: CoreConfig

    /**
     * Storage manager
     */
    val storageManager: StorageManager

    /**
     * Cache to lookup Names <> UUIDs
     */
    val corePlayerData: CorePlayerData

    /**
     * Chunk ticket manager
     */
    val chunkTicketManager: ChunkTicketManager

    /**
     * Reloads the core configuration settings of the plugin.
     */
    fun reloadCoreConfig()
}
