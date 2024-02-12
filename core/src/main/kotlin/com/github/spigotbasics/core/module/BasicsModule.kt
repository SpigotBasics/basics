package com.github.spigotbasics.core.module

import com.github.spigotbasics.core.BasicsPlugin
import com.github.spigotbasics.core.NamespacedNamespacedKeyFactory
import com.github.spigotbasics.core.command.BasicsCommandBuilder
import com.github.spigotbasics.core.command.BasicsCommandManager
import com.github.spigotbasics.core.command.ParsedCommandBuilder
import com.github.spigotbasics.core.command.parsed.ParsedCommandContext
import com.github.spigotbasics.core.config.ConfigName
import com.github.spigotbasics.core.config.SavedConfig
import com.github.spigotbasics.core.event.BasicsEventBus
import com.github.spigotbasics.core.logger.BasicsLogger
import com.github.spigotbasics.core.messages.CoreMessages
import com.github.spigotbasics.core.messages.MessageFactory
import com.github.spigotbasics.core.messages.tags.TagResolverFactory
import com.github.spigotbasics.core.module.loader.ModuleJarClassLoader
import com.github.spigotbasics.core.permission.BasicsPermissionManager
import com.github.spigotbasics.core.scheduler.BasicsScheduler
import com.github.spigotbasics.core.storage.NamespacedStorage
import org.bukkit.Server
import org.bukkit.permissions.Permission
import java.io.File
import java.util.UUID
import java.util.concurrent.CompletableFuture

/**
 * Represents a module. Implementations require a public constructor that takes in a [com.github.spigotbasics.core.module.loader.ModuleInstantiationContext].
 *
 * @constructor Create empty Basics plugin
 */
interface BasicsModule {
    // ----- Global instances -----

    /**
     * Basics instance
     */
    val plugin: BasicsPlugin

    /**
     * The global Message Factory
     */
    val messageFactory: MessageFactory

    /**
     * The CoreMessages instance
     */
    val coreMessages: CoreMessages

    /**
     * The global Tag Resolver Factory
     */
    val tagResolverFactory: TagResolverFactory

    /**
     * Bukkit Server instance
     */
    val server: Server

    // ----- Module instances -----

    /**
     * This module's .jar file
     */
    val file: File

    /**
     * This module's class loader
     */
    val moduleClassLoader: ModuleJarClassLoader

    /**
     * Info about this module
     */
    val info: ModuleInfo

    /**
     * This module's config
     */
    val config: SavedConfig

    /**
     * This module's message config
     */
    val messages: SavedConfig

    /**
     * This module's Logger
     */
    val logger: BasicsLogger

    /**
     * This module's scheduler
     */
    val scheduler: BasicsScheduler

    /**
     * This module's command manager
     */
    val commandManager: BasicsCommandManager

    /**
     * This module's event bus
     */
    val eventBus: BasicsEventBus

    /**
     * This module's permission manager
     */
    val permissionManager: BasicsPermissionManager

    /**
     * This module's key factory
     */
    val keyFactory: NamespacedNamespacedKeyFactory

    // ----- Module lifecycle -----

    /**
     * Called when the module is enabled
     *
     */
    fun onEnable() = Unit

    /**
     * Called when the module is disabled
     */
    fun onDisable() = Unit

    fun reloadConfig()

    // ----- Enabling / disabling methods -----

    fun enable(reloadConfig: Boolean)

    fun disable(): CompletableFuture<Void?>

    fun isEnabled(): Boolean

    // ----- PlayerData methods -----

    fun loadPlayerData(uuid: UUID): CompletableFuture<Void?> = CompletableFuture.completedFuture(null)

    fun savePlayerData(uuid: UUID): CompletableFuture<Void?> = CompletableFuture.completedFuture(null)

    fun forgetPlayerData(uuid: UUID) = Unit

    fun loadAllOnlinePlayerData(): CompletableFuture<Void?>

    fun saveAndForgetAllOnlinePlayerData(): CompletableFuture<Void?>

    // ----- Utility methods -----

    fun createCommand(
        name: String,
        permission: Permission,
    ): BasicsCommandBuilder

    fun <T : ParsedCommandContext> createParsedCommand(
        name: String,
        permission: Permission,
    ): ParsedCommandBuilder<T>

    fun createStorage(name: String? = null): NamespacedStorage

    fun getConfig(configName: ConfigName): SavedConfig

    fun <T : SavedConfig> getConfig(
        configName: ConfigName,
        configurationClass: Class<T>,
    ): T

    /**
     * Get namespaced resource name in the format <module-name>-<path>. Also removes leading slashes.
     *
     * @param path Path to the resource
     * @return Namespaced resource name
     */
    fun getNamespacedResourceName(path: String): String {
        var newPath = path

        if (path.startsWith("/")) {
            newPath = path.substring(1)
        }

        return "${info.name}-$newPath"
    }
}
