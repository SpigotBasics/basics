package com.github.spigotbasics.core.config

import com.github.spigotbasics.core.extensions.mini
import com.github.spigotbasics.core.minimessage.TagResolverFactory
import net.kyori.adventure.audience.Audience
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.JoinConfiguration
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
    val tagResolverFactory: TagResolverFactory? = null,
    private var concerns: Player? = null
) {
    companion object {
        /**
         * Represents a disabled message that will not do anything when sent
         */
        val DISABLED = Message(emptyList())
    }

    internal constructor(
        tagResolverFactory: TagResolverFactory?,
        line: String
    ) : this(tagResolverFactory = tagResolverFactory, lines = listOf(line))

//    private constructor(tagResolverFactory: TagResolverFactory) : this(
//        tagResolverFactory = tagResolverFactory,
//        lines = emptyList()
//    )

    //private val concerns: Player? = null

    fun concerns(player: Player?): Message {
        return Message(tagResolverFactory = tagResolverFactory, lines = lines, concerns = player)
    }

    /**
     * Applies the given transform to each line of the message
     *
     * @param transform Transform to apply
     * @return The message with the transform applied
     */
    fun map(transform: (String) -> String): Message {
        return Message(tagResolverFactory = tagResolverFactory, lines = lines.map(transform), concerns = concerns)
    }

//    /**
//     * Applies PlaceholderAPI placeholders to each line of the message
//     *
//     * @param player Player to apply placeholders for, or null
//     * @return The message with placeholders applied
//     */
//    fun papi(player: OfflinePlayer?): Message {
//        return Message(lines.map { it.papi(player) })
//    }

    /**
     * Parses the message from MiniMessage format to Components and sends them to the given audience
     *
     * @param receiver Audience to send the message to
     */
    fun sendTo(receiver: Audience) {
        // lines.map { it.papi(concerns) }.forEach { receiver.sendMessage(it.miniComponents(concerns)) } // TODO: Old
//        val resolvers = tagResolverFactory?.getTagResolvers(concerns)?.toTypedArray() ?: emptyArray()
//        lines.forEach { receiver.sendMessage(mini.deserialize(it, *resolvers)) }
        receiver.sendMessage(toComponent())
    }

    fun toComponents(): List<Component> {
        val resolvers = tagResolverFactory?.getTagResolvers(concerns)?.toTypedArray() ?: emptyArray()
        return lines.map { mini.deserialize(it, *resolvers) }
    }

    fun toComponent(): Component {
        return Component.join(JoinConfiguration.newlines(), toComponents())
    }


}