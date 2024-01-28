package com.github.spigotbasics.pipe

import com.github.spigotbasics.common.Either
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

interface SpigotPaperFacade {

    fun setJoinMessage(event: PlayerJoinEvent, legacy: String, miniMessage: SerializedMiniMessage)

    fun setQuitMessage(event: PlayerQuitEvent, legacy: String, miniMessage: SerializedMiniMessage)

    fun getDisplayName(player: Player): Either<String, SerializedMiniMessage>

}