package com.github.spigotbasics.modules.basicschatformat

import com.github.spigotbasics.core.Spiper
import com.github.spigotbasics.core.messages.Message
import com.github.spigotbasics.core.extensions.getAsNewLineSeparatedString
import com.github.spigotbasics.core.module.AbstractBasicsModule
import com.github.spigotbasics.core.module.loader.ModuleInstantiationContext
import org.bukkit.event.EventPriority
import org.bukkit.event.player.AsyncPlayerChatEvent


private fun String.escapeFormat(): String {
    return this.replace("%", "%%")
}

class BasicsChatFormatModule(context: ModuleInstantiationContext) : AbstractBasicsModule(context) {

    val format: Message
        get() = config.getMessage("chat-format")

    val formatAsStr: String
        get() = config.getAsNewLineSeparatedString("chat-format")

    override fun onEnable() {
        if(Spiper.isPaper) {
            eventBus.subscribe(PaperChatEventListener(this))
        } else {
            eventBus.subscribe(AsyncPlayerChatEvent::class.java, this::changeChatFormatSpigot, EventPriority.HIGHEST)
        }
    }

    fun changeChatFormatSpigot(event: AsyncPlayerChatEvent) {
        event.format = format
            .concerns(event.player)
            .tagUnparsed("message", event.message) // TODO: To allow MiniMessage in chat, this should be parsed.
            .toLegacyString().escapeFormat()
    }

}