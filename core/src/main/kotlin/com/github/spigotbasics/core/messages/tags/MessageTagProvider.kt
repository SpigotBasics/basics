package com.github.spigotbasics.core.messages.tags

interface MessageTagProvider {
    fun getMessageTags(): List<CustomTag>

    fun getTagProviders(): List<Any> = emptyList()
}
