package com.github.spigotbasics.core.playerdata

import com.github.spigotbasics.core.logger.BasicsLoggerFactory
import com.github.spigotbasics.core.messages.CoreMessages
import com.github.spigotbasics.core.module.manager.ModuleManager
import com.github.spigotbasics.core.storage.StorageConfig
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerPreLoginEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import java.util.*
import java.util.concurrent.*

class ModulePlayerDataLoader(
    storageConfig: StorageConfig,
    private val moduleManager: ModuleManager,
    private val messages: CoreMessages
) : Listener {

    private val logger = BasicsLoggerFactory.getCoreLogger(ModulePlayerDataLoader::class)

    private val joinCacheDuration = storageConfig.joinCacheDuration
    private val joinTimeOut = storageConfig.joinTimeOut

    val cachedLoginData = ConcurrentHashMap<UUID, CompletableFuture<Void?>>()
    val scheduledClearCacheFutures = ConcurrentHashMap<UUID, ScheduledFuture<*>>()

    private val scheduler: ScheduledExecutorService = Executors.newScheduledThreadPool(1)

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    fun onPlayerLogin(event: AsyncPlayerPreLoginEvent) {
        if (event.loginResult != AsyncPlayerPreLoginEvent.Result.ALLOWED) {
            return
        }
        val uuid = event.uniqueId

        val future = cachedLoginData.computeIfAbsent(uuid) {
            val newFuture = loadAll(uuid)
            scheduledClearCacheFutures[uuid] = scheduler.schedule({
                cachedLoginData.remove(uuid)?.cancel(true)
                forgetAll(uuid)
                scheduledClearCacheFutures.remove(uuid)
            }, joinCacheDuration, TimeUnit.MILLISECONDS)
            newFuture
        }

        var secondsTaken = 0.0
        while (!future.isDone && secondsTaken < joinTimeOut * 1000) {
            Thread.sleep(20)
            secondsTaken += 0.02
        }

        if (!future.isDone) {
            logger.warning("Could not load data for joining player ${event.name} in time (threshold: $joinTimeOut ms as defined in storage.yml), kicking them now.")
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, messages.failedToLoadDataOnJoin.toLegacy())
            return
        }
    }

    private fun loadAll(uuid: UUID): CompletableFuture<Void?> {
        val futures = mutableListOf<CompletableFuture<Void?>>()
        moduleManager.enabledModules.forEach { module ->
            futures += module.loadPlayerData(uuid)
        }
        return CompletableFuture.allOf(*futures.toTypedArray())
    }

    private fun forgetAll(uuid: UUID) {
        moduleManager.enabledModules.forEach { module ->
            module.forgetPlayerData(uuid)
        }
    }

    // TODO: Call this on shutdown - and on /reload? What about /module disable ?
    private fun saveAndForgetAll(uuid: UUID): CompletableFuture<Void?> {
        val futures = mutableListOf<CompletableFuture<Void?>>()
        moduleManager.enabledModules.forEach { module ->
            futures += module.savePlayerData(uuid).whenComplete { _, _ ->
                module.forgetPlayerData(uuid)
            }
        }
        return CompletableFuture.allOf(*futures.toTypedArray())
    }

    @EventHandler(priority = EventPriority.MONITOR)
    fun onPlayerJoin(event: PlayerJoinEvent) {
        val future = cachedLoginData.remove(event.player.uniqueId)
        val removeFuture = scheduledClearCacheFutures.remove(event.player.uniqueId)
        removeFuture?.cancel(true)

        // TODO: Bypass permission to join anyway

        if (future == null) {
            event.player.kickPlayer(messages.failedToLoadDataOnJoin.toLegacy() + " (Error: No future found)")
            logger.severe("Player ${event.player.name} made it to PlayerJoinEvent despite not having any data loaded (No future found), kicking them now.")
            return
        }
        if (!future.isDone) {
            event.player.kickPlayer(messages.failedToLoadDataOnJoin.toLegacy() + " (Error: Future not done)")
            logger.severe("Player ${event.player.name} made it to PlayerJoinEvent despite not having any data loaded (Future not done), kicking them now.")
            return
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    fun onPlayerQuit(event: PlayerQuitEvent) {
        saveAndForgetAll(event.player.uniqueId)
    }

    fun shutdownScheduler() {
        scheduler.shutdown()
    }

}