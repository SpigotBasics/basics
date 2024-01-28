package com.github.spigotbasics.pipe.spigot

import com.github.spigotbasics.common.Either
import com.github.spigotbasics.pipe.SerializedMiniMessage
import com.github.spigotbasics.pipe.SpigotPaperFacade
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

class SpigotFacade : SpigotPaperFacade {
    override fun setJoinMessage(event: PlayerJoinEvent, legacy: String, miniMessage: SerializedMiniMessage) {
        event.joinMessage = legacy
    }

    override fun setQuitMessage(event: PlayerQuitEvent, legacy: String, miniMessage: SerializedMiniMessage) {
        event.quitMessage = legacy
    }

    override fun getDisplayName(player: Player): Either<String, SerializedMiniMessage> {
        return Either.Left(player.displayName)
    }
}