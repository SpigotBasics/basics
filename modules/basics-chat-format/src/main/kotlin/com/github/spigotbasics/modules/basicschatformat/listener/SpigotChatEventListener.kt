package com.github.spigotbasics.modules.basicschatformat.listener

import com.github.spigotbasics.modules.basicschatformat.BasicsChatFormatModule
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerChatEvent

private fun String.escapeFormat(): String {
    return this.replace("%", "%%")
}

class SpigotChatEventListener(private val module: BasicsChatFormatModule) : Listener {
    @EventHandler
    fun onSpigotChat(event: AsyncPlayerChatEvent) {
        event.format =
            module.chatFormat.concerns(event.player)
                .tagUnparsed("message", event.message) // TODO: To allow MiniMessage in chat, this should be parsed.
                .tagParsed("message-color", "<${module.chatFormatStore.getChatDataOrDefault(event.player.uniqueId).color}>").toLegacyString()
                .escapeFormat()
    }
}
