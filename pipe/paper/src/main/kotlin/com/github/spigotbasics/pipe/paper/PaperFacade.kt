package com.github.spigotbasics.pipe.paper

import com.github.spigotbasics.common.Either
import com.github.spigotbasics.pipe.OpenInventoryFacade
import com.github.spigotbasics.pipe.SerializedMiniMessage
import com.github.spigotbasics.pipe.SpigotPaperFacade
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.Bukkit
import org.bukkit.command.SimpleCommandMap
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.plugin.PluginManager

class PaperFacade : SpigotPaperFacade {

    private val mini = MiniMessage.miniMessage()
    override fun setJoinMessage(event: PlayerJoinEvent, legacy: String, miniMessage: SerializedMiniMessage) {
        event.joinMessage(mini.deserialize(miniMessage.value))
    }

    override fun setQuitMessage(event: PlayerQuitEvent, legacy: String, miniMessage: SerializedMiniMessage) {
        event.quitMessage(mini.deserialize(miniMessage.value))
    }

    override fun getDisplayName(player: Player): Either<String, SerializedMiniMessage> {
        return Either.Right(SerializedMiniMessage(mini.serialize(player.displayName())))
    }

    override fun getCommandMap(pluginManager: PluginManager): SimpleCommandMap {
        return Bukkit.getCommandMap() as SimpleCommandMap
    }

    override val openInventoryFacade: OpenInventoryFacade = PaperOpenInventoryFacade
}