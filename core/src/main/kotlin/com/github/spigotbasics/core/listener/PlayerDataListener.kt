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
    // TODO: Another list for loaded playerdata to avoid "no future found" when the future loads successfully between PreLogin and JoinEvent
    private val scheduler: ScheduledExecutorService = Executors.newScheduledThreadPool(1)

    @EventHandler(ignoreCancelled = true)
    fun onPlayerLogin(event: AsyncPlayerPreLoginEvent) {
        if(event.loginResult != AsyncPlayerPreLoginEvent.Result.ALLOWED) {
            return
        }
        val uuid = event.uniqueId

        println("Player is joining, checking if we already try to get their data...")

        val future = if(cachedLoginData.containsKey(uuid)) {
            println("Yes, we already try to get their data, getting existing future ...")
            cachedLoginData[uuid]!!
        } else {
            println("No, we don't try to get their data, creating new future ...")
            val newFuture = loadAll(uuid)
            cachedLoginData[uuid] = newFuture
            scheduler.schedule({ // TODO: We must also store this task and cancel it to prevent issues with "No future found" message
                println("10 seconds over, removing future from cache...")
                if(cachedLoginData.containsKey(uuid)) {
                    println("Future still in cache, cancelling it and forgetting all...")
                    cachedLoginData.remove(uuid)?.cancel(true)
                    forgetAll(uuid)
                } else {
                    println("Future not in cache anymore, looks like the player joined successfully.")
                }
            }, 10, TimeUnit.SECONDS) // TODO: Configurable cache duration
            newFuture
        }
        var secondsTaken = 0.0
        while(!future.isDone && secondsTaken < 1) { // TODO: Configurable timeout
            println("Waiting 100ms for player data to load...") // TODO: Print this only in debug
            Thread.sleep(100)
            secondsTaken += 0.1
        }
        println("1 second over, checking if future is done...")
        if(!future.isDone) {
            println("Future not done, disallowing join...")
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, "[Basics] Failed to load your data, please try again.")
            return
        } else {
            println("Future done, allowing join!")
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

        // TODO: Bypass permission to join anyway
        println("ACTUAL JOIN EVENT")

        if(future == null) {
            println("NO FUTURE FOUND")
            event.player.sendMessage("[Basics] Failed to load your data, please try again. (No future found)")
            return
        }
        if(!future.isDone) {
            println("FUTURE NOT DONE")
            event.player.sendMessage("[Basics] Failed to load your data, please try again. (Future not done)")
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