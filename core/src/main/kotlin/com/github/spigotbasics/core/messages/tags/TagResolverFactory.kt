package com.github.spigotbasics.core.messages.tags

import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver

interface TagResolverFactory {
    fun createMessageSpecificPlaceholderUnparsed(tag: String, value: String): TagResolver
    fun createMessageSpecificPlaceholderParsed(tag: String, value: String): TagResolver
    fun createMessageSpecificPlaceholderMessage(tag: String, value: String): TagResolver

}
