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
            // TODO: See comment below
            futures += savePlayerData(player.uniqueId).whenComplete { _, _ -> // This was line 124 from below stacktrace
                forgetPlayerData(player.uniqueId)
            }
        }
        return CompletableFuture.allOf(*futures.toTypedArray())
    }

    /*
    TODO: forEach { } in saveAndForgetAllOnlinePlayerData again causes issues on Paper when it's first loaded during shutdown

    [09:27:15 ERROR]: Error occurred while disabling Basics v1.0-SNAPSHOT
java.lang.NoClassDefFoundError: com/github/spigotbasics/core/module/BasicsModule$saveAndForgetAllOnlinePlayerData$1$1
        at com.github.spigotbasics.core.module.BasicsModule$DefaultImpls.saveAndForgetAllOnlinePlayerData(BasicsModule.kt:124) ~[basics-1.0-SNAPSHOT-shaded.jar:?]
        at com.github.spigotbasics.core.module.AbstractBasicsModule.saveAndForgetAllOnlinePlayerData(AbstractBasicsModule.kt:15) ~[basics-1.0-SNAPSHOT-shaded.jar:?]
        at com.github.spigotbasics.core.module.manager.ModuleManager.disableModule(ModuleManager.kt:157) ~[basics-1.0-SNAPSHOT-shaded.jar:?]
        at com.github.spigotbasics.core.module.manager.ModuleManager.disableAllModules(ModuleManager.kt:195) ~[basics-1.0-SNAPSHOT-shaded.jar:?]
        at com.github.spigotbasics.core.module.manager.ModuleManager.disableAndUnloadAllModules(ModuleManager.kt:202) ~[basics-1.0-SNAPSHOT-shaded.jar:?]
        at com.github.spigotbasics.plugin.BasicsPluginImpl.onDisable(BasicsPluginImpl.kt:109) ~[basics-1.0-SNAPSHOT-shaded.jar:?]
        at org.bukkit.plugin.java.JavaPlugin.setEnabled(JavaPlugin.java:283) ~[paper-api-1.20.4-R0.1-SNAPSHOT.jar:?]
        at io.papermc.paper.plugin.manager.PaperPluginInstanceManager.disablePlugin(PaperPluginInstanceManager.java:225) ~[paper-1.20.4.jar:git-Paper-388]
        at io.papermc.paper.plugin.manager.PaperPluginInstanceManager.disablePlugins(PaperPluginInstanceManager.java:149) ~[paper-1.20.4.jar:git-Paper-388]
        at io.papermc.paper.plugin.manager.PaperPluginManagerImpl.disablePlugins(PaperPluginManagerImpl.java:92) ~[paper-1.20.4.jar:git-Paper-388]
        at org.bukkit.plugin.SimplePluginManager.disablePlugins(SimplePluginManager.java:528) ~[paper-api-1.20.4-R0.1-SNAPSHOT.jar:?]
        at org.bukkit.craftbukkit.v1_20_R3.CraftServer.disablePlugins(CraftServer.java:568) ~[paper-1.20.4.jar:git-Paper-388]
        at net.minecraft.server.MinecraftServer.stopServer(MinecraftServer.java:976) ~[paper-1.20.4.jar:git-Paper-388]
        at net.minecraft.server.dedicated.DedicatedServer.stopServer(DedicatedServer.java:821) ~[paper-1.20.4.jar:git-Paper-388]
        at net.minecraft.server.MinecraftServer.runServer(MinecraftServer.java:1253) ~[paper-1.20.4.jar:git-Paper-388]
        at net.minecraft.server.MinecraftServer.lambda$spin$0(MinecraftServer.java:321) ~[paper-1.20.4.jar:git-Paper-388]
        at java.lang.Thread.run(Thread.java:833) ~[?:?]
Caused by: java.lang.ClassNotFoundException: com.github.spigotbasics.core.module.BasicsModule$saveAndForgetAllOnlinePlayerData$1$1
        at org.bukkit.plugin.java.PluginClassLoader.loadClass0(PluginClassLoader.java:197) ~[paper-api-1.20.4-R0.1-SNAPSHOT.jar:?]
        at org.bukkit.plugin.java.PluginClassLoader.loadClass(PluginClassLoader.java:164) ~[paper-api-1.20.4-R0.1-SNAPSHOT.jar:?]
        at java.lang.ClassLoader.loadClass(ClassLoader.java:520) ~[?:?]
        ... 17 more

     */



}
