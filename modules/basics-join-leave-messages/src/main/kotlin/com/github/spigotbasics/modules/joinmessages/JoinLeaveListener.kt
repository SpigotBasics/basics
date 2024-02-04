package com.github.spigotbasics.modules.joinmessages

import com.github.spigotbasics.core.config.SavedConfig
import com.github.spigotbasics.pipe.SpigotPaperFacade
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

class JoinLeaveListener(
    private val facade: SpigotPaperFacade,
    private val config: SavedConfig,
) {

    val joinMsg
        get() = config.getMessage("join")
    val quitMsg
        get() = config.getMessage("quit")

    fun joinMessage(event: PlayerJoinEvent) {
        val msg = joinMsg.concerns(event.player)
        facade.setJoinMessage(event, msg.toLegacyString(), msg.serialize())
    }

    fun leaveMessage(event: PlayerQuitEvent) {
        val msg = quitMsg.concerns(event.player)
        facade.setQuitMessage(event, msg.toLegacyString(), msg.serialize())
    }

}