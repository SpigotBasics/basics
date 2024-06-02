package com.github.spigotbasics.modules.basicschatformat.listener

import com.github.spigotbasics.modules.basicschatformat.BasicsChatFormatModule
import com.github.spigotbasics.pipe.SerializedMiniMessage
import com.github.spigotbasics.pipe.paper.NativeComponentConverter
import io.papermc.paper.event.player.AsyncChatEvent
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

class PaperChatEventListener(private val module: BasicsChatFormatModule) : Listener {
    private val mini = MiniMessage.miniMessage()

    @EventHandler
    fun onPaperChat(event: AsyncChatEvent) {
        event.renderer { player, _, message, _ ->
            formatMessage(player, message)
        }
    }

    private fun formatMessage(
        player: Player,
        message: Component,
    ): Component {
        val serialized =
            module.chatFormat.concerns(player)
                .tagMiniMessage("message", SerializedMiniMessage(mini.serialize(message)))
                .tagParsed("message-color", "<${module.chatFormatStore.getChatDataOrDefault(player.uniqueId).color}}>")
                .serialize()
        val component = NativeComponentConverter.toNativeComponent(serialized)
        return component
    }
}
