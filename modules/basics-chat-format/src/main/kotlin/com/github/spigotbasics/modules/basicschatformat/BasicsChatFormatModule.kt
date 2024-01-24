package com.github.spigotbasics.modules.basicschatformat

import com.github.spigotbasics.core.config.Message
import com.github.spigotbasics.core.extensions.getAsNewLineSeparatedString
import com.github.spigotbasics.core.extensions.toLegacy
import com.github.spigotbasics.core.module.AbstractBasicsModule
import com.github.spigotbasics.core.module.ModuleInstantiationContext
import io.papermc.lib.PaperLib
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
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
        if(PaperLib.isPaper()) {
            eventBus.subscribe(PaperChatEventListener(this))
        } else {
            eventBus.subscribe(AsyncPlayerChatEvent::class.java, this::changeChatFormatSpigot, EventPriority.HIGHEST)
        }
    }

    fun changeChatFormatSpigot(event: AsyncPlayerChatEvent) {
        //event.format = MiniMessage.miniMessage().deserialize()//format.concerns(event.player).toComponent().toLegacy().escapeFormat()
        val originalFormatters = tagResolverFactory.getTagResolvers(event.player)
        val msgFormatter = Placeholder.unparsed("message", event.message)
        val combinedFormatters = originalFormatters + msgFormatter
        event.format = MiniMessage.miniMessage().deserialize(formatAsStr, *combinedFormatters.toTypedArray()).toLegacy().escapeFormat()
    }

}