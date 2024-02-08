package com.github.spigotbasics.core.messages

import com.github.spigotbasics.core.messages.tags.MESSAGE_SPECIFIC_TAG_PREFIX
import com.github.spigotbasics.core.messages.tags.TagResolverFactory

class MessageFactory(
    private val audienceProvider: AudienceProvider,
    private val tagResolverFactory: TagResolverFactory,
) {
    companion object {
        private const val UNPARSED = "__unparsed__"
        private const val UNPARSED_TAG = "<${MESSAGE_SPECIFIC_TAG_PREFIX}$UNPARSED>"
    }

    fun createMessage(vararg text: String): Message {
        return Message(text.asList(), audienceProvider, tagResolverFactory)
    }

    fun createPlainMessage(text: String): Message {
        return Message(listOf(UNPARSED_TAG), audienceProvider, tagResolverFactory).tagUnparsed(UNPARSED, text)
    }

    fun createMessage(text: List<String>): Message {
        return Message(text, audienceProvider, tagResolverFactory)
    }
}
