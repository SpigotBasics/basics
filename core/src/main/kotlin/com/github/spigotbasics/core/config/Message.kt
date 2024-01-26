package com.github.spigotbasics.core.config

import com.github.spigotbasics.core.extensions.mini
import com.github.spigotbasics.core.minimessage.TagResolverFactory
import net.kyori.adventure.audience.Audience
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.JoinConfiguration
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver
import org.bukkit.entity.Player

/**
 * Represents a message containing of zero or more lines
 *
 * See [SavedConfig.getMessage] on how to obtain a message
 *
 * @property lines Lines of the message
 * @constructor Create a new message
 */
data class Message(
    var lines: List<String>,
    val tagResolverFactory: TagResolverFactory?,
    private var concerns: Player? = null,
    private val customTagResolvers: MutableList<TagResolver> = mutableListOf()
) {
    companion object {
        /**
         * Represents a disabled message that will not do anything when sent
         */
        val DISABLED = Message(
            lines = emptyList(),
            tagResolverFactory = null)
    }

    internal constructor(
        tagResolverFactory: TagResolverFactory?,
        line: String
    ) : this(tagResolverFactory = tagResolverFactory,
        lines = listOf(line))

    fun concerns(player: Player?): Message {
        this.concerns = player
        return this
    }

    /**
     * Applies the given transform to each line of the message
     *
     * @param transform Transform to apply
     * @return The message with the transform applied
     */
    fun map(transform: (String) -> String): Message {
        lines = lines.map(transform)
        return this
    }

    fun tags(tags: Collection<TagResolver>): Message {
        customTagResolvers.addAll(tags)
        return this
    }

    fun tags(vararg tags: Pair<String, String>): Message {
        return tags(tags.map { (key, value) -> Placeholder.unparsed(key, value) })
    }

    fun tags(tags: Map<String, String>): Message {
        return tags(tags.map { (key, value) -> Placeholder.unparsed(key, value) })
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