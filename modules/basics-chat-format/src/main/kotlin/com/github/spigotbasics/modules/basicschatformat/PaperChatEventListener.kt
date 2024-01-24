package com.github.spigotbasics.modules.basicschatformat

import io.papermc.paper.event.player.AsyncChatEvent
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

class PaperChatEventListener(private val module: BasicsChatFormatModule) : Listener {

    @EventHandler
    fun onPaperChat(event: AsyncChatEvent) {
        event.renderer { player, _, message, _ ->
            formatMessage(player, message)
        }

    }

    fun formatMessage(player: Player, message: Component): Component {
        val originalFormatters = module.tagResolverFactory.getTagResolvers(player)
        val msgFormatter = Placeholder.component("message", message)
        val combinedFormatters = originalFormatters + msgFormatter
        return MiniMessage.miniMessage().deserialize(module.formatAsStr, *combinedFormatters.toTypedArray())
    }

}


