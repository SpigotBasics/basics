package com.github.spigotbasics.core.messages.tags

import net.kyori.adventure.text.minimessage.internal.parser.TokenParser.TagProvider
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver

interface MessageTagProvider {
    fun getMessageTags(): List<CustomTag>

    fun getTagProviders(): List<Any> = emptyList()
}
