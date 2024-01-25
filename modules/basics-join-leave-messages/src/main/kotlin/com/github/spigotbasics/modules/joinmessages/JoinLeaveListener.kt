package com.github.spigotbasics.modules.joinmessages

import com.github.spigotbasics.core.facade.event.PlayerJoinEventSetMessageAccess
import com.github.spigotbasics.core.facade.event.PlayerQuitEventSetMessageAccess
import com.github.spigotbasics.core.config.SavedModuleConfig
import com.github.spigotbasics.core.extensions.toLegacy
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

class JoinLeaveListener(
    private val config: SavedModuleConfig,
) {

    val joinMsg
        get() = config.getMessage("join")
    val quitMsg
        get() = config.getMessage("quit")

    fun joinMessage(event: PlayerJoinEvent) {
        val msg = joinMsg.concerns(event.player).toComponent()
        PlayerJoinEventSetMessageAccess.apply(event, msg, msg.toLegacy())

    }

    fun leaveMessage(event: PlayerQuitEvent) {
        val msg = quitMsg.concerns(event.player).toComponent()
        PlayerQuitEventSetMessageAccess.apply(event, msg, msg.toLegacy())
    }

}