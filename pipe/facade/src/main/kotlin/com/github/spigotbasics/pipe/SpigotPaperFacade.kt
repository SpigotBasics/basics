package com.github.spigotbasics.pipe

import com.github.spigotbasics.common.Either
import org.bukkit.command.SimpleCommandMap
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.plugin.PluginManager

interface SpigotPaperFacade {

    fun setJoinMessage(event: PlayerJoinEvent, legacy: String, miniMessage: SerializedMiniMessage)

    fun setQuitMessage(event: PlayerQuitEvent, legacy: String, miniMessage: SerializedMiniMessage)

    fun getDisplayName(player: Player): Either<String, SerializedMiniMessage>

    fun getCommandMap(pluginManager: PluginManager): SimpleCommandMap

}