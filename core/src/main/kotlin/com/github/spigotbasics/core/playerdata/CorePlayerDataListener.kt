package com.github.spigotbasics.core.playerdata

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

class CorePlayerDataListener(private val corePlayerData: CorePlayerData) : Listener{

    @EventHandler
    internal fun onJoin(event: PlayerJoinEvent) {
        corePlayerData.storeNameAndUuid(event.player.name, event.player.uniqueId)
    }

}