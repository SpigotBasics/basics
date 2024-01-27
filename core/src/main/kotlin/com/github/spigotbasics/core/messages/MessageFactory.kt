package com.github.spigotbasics.core.messages

import com.github.spigotbasics.core.BasicsPlugin
import net.kyori.adventure.platform.bukkit.BukkitAudiences
import net.kyori.adventure.text.minimessage.MiniMessage

class MessageFactory(
    private val audienceProvider: AudienceProvider,
    private val tagResolverFactory: TagResolverFactory) {

    //private val miniMessage = MiniMessage.miniMessage()

    fun createMessage(text: String): Message {
        return Message(listOf(text), audienceProvider, tagResolverFactory)
    }

    fun createMessage(text: List<String>): Message {
        return Message(text, audienceProvider, tagResolverFactory)
    }

}