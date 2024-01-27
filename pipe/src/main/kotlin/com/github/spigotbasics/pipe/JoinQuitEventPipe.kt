package com.github.spigotbasics.pipe

import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

object JoinQuitEventPipe {

    fun setJoinMessage(isPaper: Boolean, event: PlayerJoinEvent, legacy: String, miniMessage: String) {
        if(isPaper) {
            event.joinMessage(MiniMessage.miniMessage().deserialize(miniMessage))
        } else {
            event.joinMessage = legacy
        }
    }

    fun setQuitMessage(isPaper: Boolean, event: PlayerQuitEvent, legacy: String, miniMessage: String) {
        if(isPaper) {
            event.quitMessage(MiniMessage.miniMessage().deserialize(miniMessage))
        } else {
            event.quitMessage = legacy
        }
    }

}