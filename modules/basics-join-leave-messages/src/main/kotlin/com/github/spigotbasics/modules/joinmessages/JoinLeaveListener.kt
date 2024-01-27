package com.github.spigotbasics.modules.joinmessages

import com.github.spigotbasics.core.config.SavedConfig
import com.github.spigotbasics.pipe.JoinQuitEventPipe
import io.papermc.lib.PaperLib
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

class JoinLeaveListener(
    private val config: SavedConfig,
) {

    val isPaper = PaperLib.isPaper()

    val joinMsg
        get() = config.getMessage("join")
    val quitMsg
        get() = config.getMessage("quit")

    fun joinMessage(event: PlayerJoinEvent) {
        val msg = joinMsg.concerns(event.player)
        JoinQuitEventPipe.setJoinMessage(isPaper, event, msg.toLegacy(), msg.serialize())
    }

    fun leaveMessage(event: PlayerQuitEvent) {
        val msg = quitMsg.concerns(event.player)
        JoinQuitEventPipe.setQuitMessage(isPaper, event, msg.toLegacy(), msg.serialize())
    }

}