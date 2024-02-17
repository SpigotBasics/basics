package com.github.spigotbasics.pipe.spigot

import com.github.spigotbasics.common.Either
import com.github.spigotbasics.nms.NMSFacade
import com.github.spigotbasics.pipe.OpenInventoryFacade
import com.github.spigotbasics.pipe.SerializedMiniMessage
import com.github.spigotbasics.pipe.SpigotPaperFacade
import org.bukkit.command.SimpleCommandMap
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.plugin.PluginManager
import org.bukkit.plugin.SimplePluginManager

class SpigotFacade(nms: NMSFacade) : SpigotPaperFacade {
    private val inventoryFacade = SpigotOpenInventoryFacade(nms)

    override fun setJoinMessage(
        event: PlayerJoinEvent,
        legacy: String,
        miniMessage: SerializedMiniMessage,
    ) {
        event.joinMessage = legacy
    }

    override fun setQuitMessage(
        event: PlayerQuitEvent,
        legacy: String,
        miniMessage: SerializedMiniMessage,
    ) {
        event.quitMessage = legacy
    }

    override fun getDisplayName(player: Player): Either<String, SerializedMiniMessage> {
        return Either.Left(player.displayName)
    }

    override fun getCommandMap(pluginManager: PluginManager): SimpleCommandMap {
        val spm = pluginManager as SimplePluginManager
        val field = spm.javaClass.getDeclaredField("commandMap")
        field.isAccessible = true
        return field.get(spm) as SimpleCommandMap
    }

    override val openInventoryFacade: OpenInventoryFacade = inventoryFacade
}
