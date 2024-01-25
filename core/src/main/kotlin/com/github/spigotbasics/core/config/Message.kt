package com.github.spigotbasics.core.config

import com.github.spigotbasics.core.extensions.mini
import com.github.spigotbasics.core.minimessage.TagResolverFactory
import net.kyori.adventure.audience.Audience
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.JoinConfiguration
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver
import org.bukkit.entity.Player

/**
 * Represents a message containing of zero or more lines
 *
 * See [SavedModuleConfig.getMessage] on how to obtain a message
 *
 * @property lines Lines of the message
 * @constructor Create a new message
 */
data class Message(
    var lines: List<String>,
    val tagResolverFactory: TagResolverFactory?,
    private var concerns: Player?,
    private val customTagResolvers: Collection<TagResolver>
) {
    companion object {
        /**
         * Represents a disabled message that will not do anything when sent
         */
        val DISABLED = Message(
            lines = emptyList(),
            tagResolverFactory = null,
            concerns = null,
            customTagResolvers = emptyList())
    }

    internal constructor(
        tagResolverFactory: TagResolverFactory?,
        line: String
    ) : this(tagResolverFactory = tagResolverFactory,
        lines = listOf(line),
        concerns = null,
        customTagResolvers = emptyList())

    fun concerns(player: Player?): Message {
        return Message(tagResolverFactory = tagResolverFactory,
            lines = lines,
            concerns = player,
            customTagResolvers = customTagResolvers)
    }

    /**
     * Applies the given transform to each line of the message
     *
     * @param transform Transform to apply
     * @return The message with the transform applied
     */
    fun map(transform: (String) -> String): Message {
        return Message(
            tagResolverFactory = tagResolverFactory,
            lines = lines.map(transform),
            concerns = concerns,
            customTagResolvers = customTagResolvers
        )
    }

    fun tags(tags: Collection<TagResolver>): Message {
        val newTagResolvers = customTagResolvers + tags
        return Message(tagResolverFactory = tagResolverFactory, lines = lines, concerns = concerns, customTagResolvers = newTagResolvers)
    }

    fun tags(vararg tags: TagResolver): Message {
        return tags(tags.toList())
    }

    /**
     * Parses the message from MiniMessage format to Components and sends them to the given audience
     *
     * @param receiver Audience to send the message to
     */
    fun sendTo(receiver: Audience) {
        receiver.sendMessage(toComponent())
    }

    private fun getAllTagResolvers(): Array<TagResolver> {
        val defaultResolvers = tagResolverFactory?.getTagResolvers(concerns) ?: emptyList()
        return (defaultResolvers + customTagResolvers).toTypedArray()
    }


    fun toComponents(): List<Component> {
        return lines.map { mini.deserialize(it, *getAllTagResolvers()) }
    }

    fun toComponent(): Component {
        return Component.join(JoinConfiguration.newlines(), toComponents())
    }


}