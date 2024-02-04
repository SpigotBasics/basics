package com.github.spigotbasics.core.messages

import com.github.spigotbasics.core.messages.tags.TagResolverFactory


class MessageFactory(
    private val audienceProvider: AudienceProvider,
    private val tagResolverFactory: TagResolverFactory
) {

    companion object {
        private const val UNPARSED = "__unparsed__"
        private const val UNPARSED_TAG = "<$UNPARSED>"
    }

    fun createMessage(text: String): Message {
        return Message(listOf(text), audienceProvider, tagResolverFactory)
    }

    fun createPlainMessage(text: String): Message {
        return Message(listOf(UNPARSED_TAG), audienceProvider, tagResolverFactory).tagUnparsed(UNPARSED, text)
    }

    fun createMessage(text: List<String>): Message {
        return Message(text, audienceProvider, tagResolverFactory)
    }

}