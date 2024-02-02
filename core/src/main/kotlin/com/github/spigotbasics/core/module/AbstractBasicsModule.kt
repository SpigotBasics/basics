package com.github.spigotbasics.core.module

import com.github.spigotbasics.core.BasicsPlugin
import com.github.spigotbasics.core.command.BasicsCommandBuilder
import com.github.spigotbasics.core.command.BasicsCommandManager
import com.github.spigotbasics.core.config.ConfigName
import com.github.spigotbasics.core.config.SavedConfig
import com.github.spigotbasics.core.event.BasicsEventBus
import com.github.spigotbasics.core.logger.BasicsLoggerFactory
import com.github.spigotbasics.core.module.loader.ModuleInstantiationContext
import com.github.spigotbasics.core.permission.BasicsPermissionManager
import com.github.spigotbasics.core.scheduler.BasicsScheduler
import com.github.spigotbasics.core.storage.NamespacedStorage
import org.bukkit.permissions.Permission
import java.util.concurrent.CompletableFuture
import java.util.concurrent.TimeUnit
import java.util.logging.Level

/**
 * Represents the main class of a module. Extending classes require a public constructor that takes in a [ModuleInstantiationContext].
 */
abstract class AbstractBasicsModule(context: ModuleInstantiationContext) : BasicsModule {


    final override val moduleClassLoader = context.classLoader
    final override val file = context.file
    final override val info = context.info
    final override val logger = BasicsLoggerFactory.getModuleLogger(this)
    final override val plugin = context.plugin
    final override val eventBus = BasicsEventBus(context.plugin)
    final override val config = getConfig(ConfigName.CONFIG)
    final override val scheduler = BasicsScheduler(plugin)
    final override val commandManager = BasicsCommandManager(plugin.facade.getCommandMap(plugin.server.pluginManager))
    final override val messageFactory = plugin.messageFactory
    final override val tagResolverFactory /*get()*/ = plugin.tagResolverFactory
    final override val permissionManager = BasicsPermissionManager(logger)

    private val storages: MutableMap<String, NamespacedStorage> = mutableMapOf()

    private var isEnabled = false

    override fun reloadConfig() {
        config.reload()
    }


    final override fun createStorage(name: String?): NamespacedStorage {
        if (!isEnabled) {
            throw IllegalStateException("Cannot create storage while module is disabled")
        }
        val toReplaceRegex = "[^a-zA-Z0-9]".toRegex()
        var namespacedName = "m_${info.name.replace(toReplaceRegex, "_")}"
        if (name != null) {
            namespacedName += "__${name.replace(toReplaceRegex, "_")}"
        }

        if (storages.containsKey(namespacedName)) {
            throw IllegalArgumentException("Storage with name '$namespacedName' already exists")
        }

        val storage: NamespacedStorage = plugin.storageManager.createStorage(namespacedName)
        storages[namespacedName] = storage
        return storage
    }


    final override fun enable(reloadConfig: Boolean) {
        isEnabled = true
        if (reloadConfig) {
            config.reload()
        }
    }

    final override fun disable(): CompletableFuture<Void?> {
        val storageShutdownFuture = shutdownAllStorages() // TODO: Configurable timeout
        scheduler.killAll()
        eventBus.dispose()
        commandManager.unregisterAll()
        permissionManager.unregisterAll()
        isEnabled = false
        return storageShutdownFuture
    }

    private fun shutdownAllStorages(): CompletableFuture<Void?> = CompletableFuture.allOf(*storages.values.map {
        it.shutdown(10, TimeUnit.SECONDS).whenComplete { _, ex ->
            if (ex != null) logger.log(Level.SEVERE, "Failed to shutdown storage ${it.namespace}", ex)
            storages.remove(it.namespace)
        }
    }.toTypedArray())

    final override fun isEnabled(): Boolean {
        return isEnabled
    }

    final override fun loadAllOnlinePlayerData(): CompletableFuture<Void?> {
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

    final override fun saveAndForgetAllOnlinePlayerData(): CompletableFuture<Void?> {
        val futures = mutableListOf<CompletableFuture<Void?>>()
        plugin.server.onlinePlayers.forEach { player -> // TODO: Log exceptions
            // TODO: See comment below
            futures += savePlayerData(player.uniqueId).whenComplete { _, _ -> // This was line 124 from below stacktrace
                forgetPlayerData(player.uniqueId)
            }
        }
        return CompletableFuture.allOf(*futures.toTypedArray())
    }

    final override fun getConfig(configName: ConfigName): SavedConfig = getConfig(configName, SavedConfig::class.java)

    final override fun <T : SavedConfig> getConfig(configName: ConfigName, configurationClass: Class<T>): T =
        plugin.coreConfigManager.getConfig(
            configName.path, getNamespacedResourceName(configName.path), javaClass, configurationClass
        )

    final override fun createCommand(name: String, permission: Permission): BasicsCommandBuilder {
        return BasicsCommandBuilder(this, name, permission)
    }

}
