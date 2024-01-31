package com.github.spigotbasics.core.module

import com.github.spigotbasics.core.BasicsPlugin
import com.github.spigotbasics.core.command.BasicsCommandBuilder
import com.github.spigotbasics.core.command.BasicsCommandManager
import com.github.spigotbasics.core.config.SavedConfig
import com.github.spigotbasics.core.event.BasicsEventBus
import com.github.spigotbasics.core.logger.BasicsLogger
import com.github.spigotbasics.core.messages.MessageFactory
import com.github.spigotbasics.core.messages.TagResolverFactory
import com.github.spigotbasics.core.module.loader.ModuleJarClassLoader
import com.github.spigotbasics.core.permission.BasicsPermissionManager
import com.github.spigotbasics.core.scheduler.BasicsScheduler
import com.github.spigotbasics.core.storage.NamespacedStorage
import java.io.File
import java.util.*
import java.util.concurrent.CompletableFuture

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
    val logger: BasicsLogger

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

    val permissionManager: BasicsPermissionManager

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
    fun disable(): CompletableFuture<Void?>

    fun isEnabled(): Boolean


    fun reloadConfig()

    fun createCommand(): BasicsCommandBuilder {
        return BasicsCommandBuilder(this)
    }

    fun createStorage(name: String? = null): NamespacedStorage

    fun loadPlayerData(uuid: UUID): CompletableFuture<Void?> = CompletableFuture.completedFuture(null)

    fun savePlayerData(uuid: UUID): CompletableFuture<Void?> = CompletableFuture.completedFuture(null)

    fun forgetPlayerData(uuid: UUID) = Unit

    fun loadAllOnlinePlayerData(): CompletableFuture<Void?> {
        val futures = mutableListOf<CompletableFuture<Void?>>()
        plugin.server.onlinePlayers.forEach { player -> // TODO: Log exceptions
            futures += loadPlayerData(player.uniqueId)
        }
        return CompletableFuture.allOf(*futures.toTypedArray())
    }

    private fun saveAllOnlinePlayerData(): CompletableFuture<Void?> {
        val futures = mutableListOf<CompletableFuture<Void?>>()
        plugin.server.onlinePlayers.forEach { player -> // TODO: Log exceptions
            futures += savePlayerData(player.uniqueId)
        }
        return CompletableFuture.allOf(*futures.toTypedArray())
    }

    fun saveAndForgetAllOnlinePlayerData(): CompletableFuture<Void?> {
        val futures = mutableListOf<CompletableFuture<Void?>>()
        plugin.server.onlinePlayers.forEach { player -> // TODO: Log exceptions
            futures += savePlayerData(player.uniqueId).whenComplete { _, _ ->
                forgetPlayerData(player.uniqueId)
            }
        }
        return CompletableFuture.allOf(*futures.toTypedArray())
    }



}
