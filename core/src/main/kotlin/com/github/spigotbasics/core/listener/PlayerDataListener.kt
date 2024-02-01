package com.github.spigotbasics.core.listener

import com.github.spigotbasics.core.module.manager.ModuleManager
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerPreLoginEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import java.util.UUID
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

class PlayerDataListener(private val moduleManager: ModuleManager) : Listener {

    private val cachedLoginData = mutableMapOf<UUID, CompletableFuture<Void?>>()
    private val scheduler: ScheduledExecutorService = Executors.newScheduledThreadPool(1)

    @EventHandler(ignoreCancelled = true)
    fun onPlayerLogin(event: AsyncPlayerPreLoginEvent) {
        if(event.loginResult != AsyncPlayerPreLoginEvent.Result.ALLOWED) {
            return
        }
        val uuid = event.uniqueId

        val future = if(cachedLoginData.containsKey(uuid)) {
            cachedLoginData[uuid]!!
        } else {
            val newFuture = loadAll(uuid)
            cachedLoginData[uuid] = newFuture
            scheduler.schedule({
                if(cachedLoginData.containsKey(uuid)) {
                    cachedLoginData[uuid]?.cancel(true)
                    forgetAll(uuid)
                }
                cachedLoginData.remove(uuid)
            }, 10, TimeUnit.SECONDS) // TODO: Configurable cache duration
            newFuture
        }
        var secondsTaken = 0.0
        while(!future.isDone && secondsTaken < 1) { // TODO: Configurable timeout
            println("Waiting for player data to load...")
            Thread.sleep(100)
            secondsTaken += 0.1
        }
        if(!future.isDone) {
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, "[Basics] Failed to load your data, please try again.")
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
        if(future == null) {
            event.player.kickPlayer("[Basics] Failed to load your data, please try again. (No future found)")
            return
        }
        if(!future.isDone) {
            event.player.kickPlayer("[Basics] Failed to load your data, please try again. (Future not done)")
            return
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    fun onPlayerQuit(event: PlayerQuitEvent) {
        saveAndForgetAll(event.player.uniqueId)
    }

    // TODO: Call this
    fun shutdownScheduler() {
        scheduler.shutdown()
    }

}