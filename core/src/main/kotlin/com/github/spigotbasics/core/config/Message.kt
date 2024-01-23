package com.github.spigotbasics.core.config

import com.github.spigotbasics.core.extensions.miniComponents
import com.github.spigotbasics.core.extensions.papi
import net.kyori.adventure.audience.Audience
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player

/**
 * Represents a message containing of zero or more lines
 *
 * See [SavedModuleConfig.getMessage] on how to obtain a message
 *
 * @property lines Lines of the message
 * @constructor Create a new message
 */
data class Message(var lines: List<String>, private var concerns: Player? = null) {
    companion object {
        /**
         * Represents a disabled message that will not do anything when sent
         */
        val DISABLED = Message()
    }

    internal constructor(line: String) : this(listOf(line))
    private constructor() : this(emptyList())

    //private val concerns: Player? = null

    fun concerns(player: Player?): Message {
        return Message(lines, player)
    }

    /**
     * Applies the given transform to each line of the message
     *
     * @param transform Transform to apply
     * @return The message with the transform applied
     */
    fun map(transform: (String) -> String): Message {
        return Message(lines.map(transform))
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
    fun sendMiniTo(receiver: Audience) {
        lines.map { it.papi(concerns) }.forEach { receiver.sendMessage(it.miniComponents(concerns)) }
    }

}