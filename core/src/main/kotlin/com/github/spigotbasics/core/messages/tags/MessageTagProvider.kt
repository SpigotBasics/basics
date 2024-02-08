package com.github.spigotbasics.core.messages.tags

interface MessageTagProvider {
    fun getMessageTags(): List<CustomTag>
}
