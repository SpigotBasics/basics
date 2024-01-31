package com.github.spigotbasics.core.listener

import com.github.spigotbasics.core.module.manager.ModuleManager
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

class JoinQuitListener(private val moduleManager: ModuleManager) : Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    fun onPlayerJoin(event: PlayerJoinEvent) {
        val player = event.player
        moduleManager.enabledModules.forEach { module ->
            module.loadPlayerData(player.uniqueId)
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    fun onPlayerQuit(event: PlayerQuitEvent) {
        val player = event.player
        moduleManager.enabledModules.forEach { module ->
            module.savePlayerData(player.uniqueId).whenComplete { _, _ ->
                module.forgetPlayerData(player.uniqueId)
            }
        }
    }

}