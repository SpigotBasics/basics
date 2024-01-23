package com.github.spigotbasics.modules.joinmessages

import com.github.spigotbasics.core.config.SavedModuleConfig
import net.kyori.adventure.platform.bukkit.BukkitAudiences
import org.bukkit.event.EventHandler
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

class JoinLeaveListener(
    private val config: SavedModuleConfig,
    private val audience: BukkitAudiences
) {

    val joinMsg
        get() = config.getMessage("join")
    val joinSelfMsg
        get() = config.getMessage("join-self")
    val joinConsoleMsg
        get() = config.getMessage("join-console")
    val quitMsg
        get() = config.getMessage("quit")
    val quitConsoleMsg
        get() = config.getMessage("quit-console")

    fun joinMessage(event: PlayerJoinEvent) {
        event.joinMessage = null

        // All players
        joinMsg.concerns(event.player).sendMiniTo(audience.players().filterAudience { it != audience.player(event.player) })

        // Self player
        joinSelfMsg.concerns(event.player).sendMiniTo(audience.player(event.player))

        // Console
        joinConsoleMsg.concerns(event.player).sendMiniTo(audience.console())

    }

    fun leaveMessage(event: PlayerQuitEvent) {
        event.quitMessage = null

        // All players
        quitMsg.concerns(event.player).sendMiniTo(audience.players())

        // Console
        quitConsoleMsg.concerns(event.player).sendMiniTo(audience.console())
    }

}